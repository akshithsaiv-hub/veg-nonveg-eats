import { Link } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { motion } from "motion/react";
import { ArrowRight, ChefHat, Clock, Star } from "lucide-react";
import { apiGet } from "@/lib/api";
import { useCart } from "@/hooks/useCart";
import FoodSection from "@/components/FoodSection";
import { type CategoryInfo, type FeaturedMenu, type MenuItem } from "@/lib/types";

// Non-blocking probes: the hero and static sections render with or without the backend.
const fetchFeatured = () => apiGet<FeaturedMenu>("/menu/featured");
const fetchCategories = () => apiGet<CategoryInfo[]>("/menu/categories");

const synthetic = (id: string, name: string, emoji: string, items: MenuItem[]): CategoryInfo => ({
  id,
  name,
  emoji,
  veg: true,
  seq: 0,
  item_count: items.length,
});

export default function Home() {
  const featured = useQuery({ queryKey: ["featured"], queryFn: fetchFeatured, retry: false });
  const cats = useQuery({ queryKey: ["menu-categories"], queryFn: fetchCategories, retry: false });
  const { data: cart } = useCart();

  const bestSellers = featured.data?.best_sellers ?? [];
  const popular = featured.data?.popular ?? [];
  const categories = cats.data ?? [];

  return (
    <div>
      {/* ---------------- Hero ---------------- */}
      <section className="relative overflow-hidden bg-gradient-to-b from-orange-50/80 to-[#FAF8F5]">
        <div className="mx-auto grid max-w-7xl items-center gap-10 px-4 pb-14 pt-12 md:px-8 lg:grid-cols-2 lg:pt-16">
          <motion.div
            initial={{ opacity: 0, y: 18 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5 }}
          >
            <span className="inline-flex items-center gap-2 rounded-full bg-green-50 px-3 py-1 text-[11px] font-bold uppercase tracking-[0.15em] text-green-700 ring-1 ring-green-200">
              🟢 Pure Veg Kitchen • 🔴 Non-Veg Specials
            </span>
            <h1 className="mt-5 font-heading text-4xl font-bold leading-tight tracking-tight text-stone-900 md:text-6xl">
              Authentic Andhra flavours,{" "}
              <span className="text-primary">delivered hot</span>
            </h1>
            <p className="mt-5 max-w-lg text-base leading-relaxed text-stone-600">
              Fluffy idlis, crispy dosas and dum biryanis straight from our kitchen —
              plus tandoor-fresh non-veg specials. Real photographs, honest prices,
              5% GST. No surprises.
            </p>
            <div className="mt-7 flex flex-wrap items-center gap-3">
              <Link
                to="/menu"
                data-testid="hero-cta-menu"
                className="inline-flex items-center gap-2 rounded-full bg-primary px-6 py-3 text-sm font-bold text-white shadow-sm transition-all hover:bg-primary/90 active:scale-95"
              >
                Browse the Menu <ArrowRight className="size-4" />
              </Link>
              <Link
                to="/menu#best-sellers"
                data-testid="hero-cta-bestsellers"
                className="inline-flex items-center gap-2 rounded-full border border-stone-300 bg-white px-6 py-3 text-sm font-bold text-stone-800 transition-all hover:border-primary hover:text-primary active:scale-95"
              >
                <Star className="size-4 text-amber-500" /> Best Sellers
              </Link>
              <Link
                to="/menu?filter=veg"
                data-testid="hero-filter-veg"
                className="rounded-full bg-green-50 px-4 py-2 text-sm font-bold text-green-700 ring-1 ring-green-200 transition-colors hover:bg-green-100"
              >
                🟢 Veg Only
              </Link>
              <Link
                to="/menu?filter=nonveg"
                data-testid="hero-filter-nonveg"
                className="rounded-full bg-red-50 px-4 py-2 text-sm font-bold text-red-600 ring-1 ring-red-200 transition-colors hover:bg-red-100"
              >
                🔴 Non-Veg
              </Link>
            </div>
            <div className="mt-8 flex flex-wrap items-center gap-x-6 gap-y-2 text-xs font-semibold text-stone-500">
              <span className="inline-flex items-center gap-1.5"><Clock className="size-4 text-primary" /> 30–40 min delivery</span>
              <span className="inline-flex items-center gap-1.5"><ChefHat className="size-4 text-primary" /> Freshly made to order</span>
              <span className="inline-flex items-center gap-1.5"><Star className="size-4 text-amber-500" /> 4.8 rated by locals</span>
            </div>
          </motion.div>

          <motion.div
            initial={{ opacity: 0, scale: 0.97 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ duration: 0.55, delay: 0.1 }}
            className="relative"
          >
            <div className="overflow-hidden rounded-3xl shadow-xl ring-1 ring-stone-200">
              <img
                src="/images/hero.jpg"
                alt="Traditional Andhra meal served on a banana leaf"
                className="h-[320px] w-full object-cover md:h-[440px]"
              />
            </div>
            <div className="absolute -bottom-5 left-5 rounded-2xl bg-white px-5 py-3.5 shadow-lg ring-1 ring-stone-200">
              <p className="text-[11px] font-bold uppercase tracking-wider text-stone-400">From ₹80</p>
              <p className="font-heading text-lg font-bold text-stone-900">Idli • Dosa • Biryani</p>
            </div>
            <div className="absolute -top-4 right-5 rounded-2xl bg-secondary px-5 py-3.5 text-white shadow-lg">
              <p className="font-mono text-xl font-bold">{cart?.count ?? 0}</p>
              <p className="text-[11px] font-semibold uppercase tracking-wider text-white/80">
                in your cart
              </p>
            </div>
          </motion.div>
        </div>
      </section>

      {/* ---------------- Categories ---------------- */}
      <section className="mx-auto max-w-7xl px-4 pt-10 md:px-8">
        <div className="flex items-end justify-between">
          <h2 className="font-heading text-2xl font-bold tracking-tight text-stone-900 md:text-3xl">
            Explore every category
          </h2>
          <Link to="/menu" className="hidden text-sm font-bold text-primary hover:underline sm:block">
            View full menu →
          </Link>
        </div>
        <div className="mt-5 grid grid-cols-2 gap-3 sm:grid-cols-3 lg:grid-cols-6">
          {(categories.length
            ? categories
            : Array.from({ length: 12 }, (_, i) => null)
          ).map((c, i) =>
            c ? (
              <Link
                key={c.id}
                to={`/menu#${c.id}`}
                data-testid={`category-tile-${c.id}`}
                className={`group rounded-2xl border p-4 text-center shadow-sm transition-all hover:-translate-y-0.5 hover:shadow-md ${
                  c.veg
                    ? "border-stone-200/80 bg-white hover:border-green-300"
                    : "border-red-100 bg-red-50/50 hover:border-red-300"
                }`}
              >
                <span className="block text-2xl transition-transform group-hover:scale-110">{c.emoji}</span>
                <span className="mt-1.5 block text-[13px] font-bold leading-tight text-stone-800">{c.name}</span>
                <span className="mt-0.5 block text-[11px] text-stone-400">{c.item_count} dishes</span>
              </Link>
            ) : (
              <div key={i} className="h-[88px] animate-pulse rounded-2xl bg-stone-100" />
            ),
          )}
        </div>
      </section>

      {/* ---------------- Best Sellers ---------------- */}
      <section className="mx-auto max-w-7xl px-4 pt-14 md:px-8">
        {featured.isLoading ? (
          <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-4">
            {Array.from({ length: 4 }).map((_, i) => (
              <div key={i} className="h-72 animate-pulse rounded-2xl bg-stone-100" />
            ))}
          </div>
        ) : bestSellers.length > 0 ? (
          <FoodSection
            category={synthetic("best-sellers", "Best Sellers", "⭐", bestSellers)}
            items={bestSellers.slice(0, 8)}
            quantities={{}}
          />
        ) : (
          <FoodSection
            category={synthetic("best-sellers", "Best Sellers", "⭐", [])}
            items={[]}
            quantities={{}}
          />
        )}
      </section>

      {/* ---------------- Popular dishes ---------------- */}
      {popular.length > 0 && (
        <section className="mx-auto max-w-7xl px-4 pt-14 md:px-8">
          <FoodSection
            category={synthetic("popular", "Popular across the kitchen", "🔥", popular)}
            items={popular.slice(0, 8)}
            quantities={{}}
          />
        </section>
      )}

      {/* ---------------- Offers strip ---------------- */}
      <section className="mx-auto mt-16 max-w-7xl px-4 md:px-8">
        <div className="flex flex-col items-start justify-between gap-4 rounded-3xl bg-secondary px-8 py-8 text-white md:flex-row md:items-center">
          <div>
            <h3 className="font-heading text-2xl font-bold">Save more on every order</h3>
            <p className="mt-1 text-sm text-white/80">
              Apply <span className="rounded bg-white/15 px-1.5 py-0.5 font-mono font-bold">WELCOME10</span> for
              10% off, or <span className="rounded bg-white/15 px-1.5 py-0.5 font-mono font-bold">FESTIVE20</span> for
              20% off above ₹499 — applied at checkout.
            </p>
          </div>
          <Link
            to="/menu"
            className="rounded-full bg-white px-6 py-3 text-sm font-bold text-secondary transition-transform active:scale-95"
          >
            Start an order →
          </Link>
        </div>
      </section>
    </div>
  );
}
