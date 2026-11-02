import { useEffect, useMemo, useState } from "react";
import { useLocation, useSearchParams } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { Search, X } from "lucide-react";
import { apiGet } from "@/lib/api";
import { useCart } from "@/hooks/useCart";
import CategoryNav from "@/components/CategoryNav";
import FoodSection from "@/components/FoodSection";
import type { CategoryInfo, MenuItem } from "@/lib/types";

type DietFilter = "all" | "veg" | "nonveg";

export default function Menu() {
  const [params] = useSearchParams();
  const location = useLocation();
  const [q, setQ] = useState(params.get("q") ?? "");
  const [diet, setDiet] = useState<DietFilter>(() => {
    const f = params.get("filter");
    return f === "veg" ? "veg" : f === "nonveg" ? "nonveg" : "all";
  });
  const [active, setActive] = useState("best-sellers");

  const menuQ = useQuery({ queryKey: ["menu"], queryFn: () => apiGet<MenuItem[]>("/menu") });
  const catsQ = useQuery({
    queryKey: ["menu-categories"],
    queryFn: () => apiGet<CategoryInfo[]>("/menu/categories"),
  });
  const { data: cart } = useCart();

  // Re-sync filters when navigated to (e.g. from the navbar search or home-page chips)
  useEffect(() => {
    setQ(params.get("q") ?? "");
    const f = params.get("filter");
    setDiet(f === "veg" ? "veg" : f === "nonveg" ? "nonveg" : "all");
  }, [params]);

  const items = menuQ.data ?? [];
  const search = q.trim().toLowerCase();

  const filtered = useMemo(
    () =>
      items.filter(
        (i) =>
          (diet === "all" || (diet === "veg" ? i.veg : !i.veg)) &&
          (!search ||
            i.name.toLowerCase().includes(search) ||
            i.description.toLowerCase().includes(search)),
      ),
    [items, diet, search],
  );

  const quantities = useMemo(
    () => Object.fromEntries((cart?.items ?? []).map((i) => [i.id, i.quantity])),
    [cart],
  );

  const categories = catsQ.data ?? [];
  const presentIds = new Set(filtered.map((i) => i.category));
  const visibleCats = categories.filter((c) => presentIds.has(c.id));

  const vegCount = items.filter((i) => i.veg).length;
  const nonvegCount = items.length - vegCount;

  // Scroll-spy: highlight the category pill whose section is at the top of the viewport
  useEffect(() => {
    const sections = Array.from(document.querySelectorAll<HTMLElement>("section[data-category]"));
    if (sections.length === 0) return;
    const obs = new IntersectionObserver(
      (entries) => {
        for (const e of entries) {
          if (e.isIntersecting) setActive((e.target as HTMLElement).dataset.category!);
        }
      },
      { rootMargin: "-110px 0px -65% 0px" },
    );
    sections.forEach((s) => obs.observe(s));
    return () => obs.disconnect();
  }, [filtered, menuQ.data]);

  // Deep-link jumps (#dosa etc.) — after data arrives
  useEffect(() => {
    if (!menuQ.data) return;
    const hash = location.hash.slice(1);
    if (hash) {
      const el = document.getElementById(hash);
      if (el) {
        const t = setTimeout(() => el.scrollIntoView({ behavior: "smooth", block: "start" }), 60);
        return () => clearTimeout(t);
      }
    }
  }, [location.hash, menuQ.data]);

  const filterBtn = (value: DietFilter, label: string, count: number, testId: string, cls: string) => (
    <button
      type="button"
      data-testid={testId}
      onClick={() => setDiet(value)}
      className={`rounded-full px-4 py-1.5 text-[13px] font-bold transition-colors ${
        diet === value ? cls : "bg-stone-100 text-stone-600 hover:bg-stone-200"
      }`}
    >
      {label} <span className="ml-0.5 font-medium opacity-70">{count}</span>
    </button>
  );

  return (
    <div>
      <CategoryNav categories={visibleCats} activeId={active} />

      <div className="mx-auto max-w-7xl px-4 pt-8 md:px-8">
        <div className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
          <div>
            <h1 className="font-heading text-3xl font-bold tracking-tight text-stone-900 md:text-4xl">
              Our Menu
            </h1>
            <p className="mt-1 text-sm text-stone-500">
              {items.length} dishes across {categories.length} categories — 🟢 veg marked green, 🔴 non-veg marked red.
            </p>
          </div>

          <div className="flex flex-col gap-3 sm:flex-row sm:items-center">
            <label className="relative block sm:w-64">
              <Search className="pointer-events-none absolute left-3 top-1/2 size-4 -translate-y-1/2 text-stone-400" />
              <input
                value={q}
                onChange={(e) => setQ(e.target.value)}
                placeholder="Search food items…"
                data-testid="search-input"
                className="w-full rounded-full border border-stone-200 bg-white py-2 pl-9 pr-9 text-sm outline-none transition-colors placeholder:text-stone-400 focus:border-primary"
              />
              {q && (
                <button
                  type="button"
                  onClick={() => setQ("")}
                  aria-label="Clear search"
                  data-testid="search-clear-button"
                  className="absolute right-2.5 top-1/2 -translate-y-1/2 rounded-full p-0.5 text-stone-400 hover:bg-stone-100"
                >
                  <X className="size-4" />
                </button>
              )}
            </label>

            <div className="flex items-center gap-1.5">
              {filterBtn("all", "All", items.length, "filter-all-button", "bg-stone-900 text-white")}
              {filterBtn("veg", "🟢 Veg", vegCount, "filter-veg-button", "bg-green-600 text-white")}
              {filterBtn("nonveg", "🔴 Non-Veg", nonvegCount, "filter-nonveg-button", "bg-red-600 text-white")}
            </div>
          </div>
        </div>

        {/* Error / empty states — page shell always renders */}
        {menuQ.isError && (
          <div className="mt-10 rounded-2xl border border-amber-200 bg-amber-50 px-6 py-10 text-center">
            <p className="font-heading text-xl font-bold text-stone-800">Menu is warming up…</p>
            <p className="mt-1 text-sm text-stone-500">
              We couldn't reach the kitchen right now. Please refresh in a moment.
            </p>
          </div>
        )}

        {menuQ.isLoading && (
          <div className="mt-10 grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
            {Array.from({ length: 8 }).map((_, i) => (
              <div key={i} className="h-72 animate-pulse rounded-2xl bg-stone-100" />
            ))}
          </div>
        )}

        {search && !menuQ.isLoading && filtered.length === 0 && (
          <div className="mt-10 rounded-2xl border border-stone-200 bg-white px-6 py-12 text-center">
            <p className="text-3xl">🍽️</p>
            <p className="mt-2 font-heading text-xl font-bold text-stone-800">
              No dishes match "{q}"
            </p>
            <p className="mt-1 text-sm text-stone-500">Try "dosa", "biryani", "paneer" or "chicken".</p>
          </div>
        )}

        <div className="mt-10 space-y-14">
          {visibleCats.map((cat) => (
            <FoodSection
              key={cat.id}
              category={cat}
              items={filtered.filter((i) => i.category === cat.id)}
              quantities={quantities}
            />
          ))}
        </div>
      </div>
    </div>
  );
}
