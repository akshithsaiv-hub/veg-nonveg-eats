import { useState, type FormEvent } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Flame, ShoppingCart } from "lucide-react";
import { useCart } from "@/hooks/useCart";
import { formatINR } from "@/lib/types";

export default function Navbar() {
  const { data: cart } = useCart();
  const [q, setQ] = useState("");
  const navigate = useNavigate();

  const count = cart?.count ?? 0;
  const subtotal = cart?.subtotal ?? 0;

  const submit = (e: FormEvent) => {
    e.preventDefault();
    navigate(q.trim() ? `/menu?q=${encodeURIComponent(q.trim())}` : "/menu");
  };

  return (
    <header className="sticky top-0 z-40 border-b border-stone-200/80 bg-white/95 shadow-xs backdrop-blur-md">
      <div className="mx-auto flex max-w-7xl items-center justify-between gap-4 px-4 py-3 md:px-8">
        <Link to="/" className="flex items-center gap-2.5" data-testid="navbar-brand">
          <span className="grid size-10 place-items-center rounded-xl bg-primary text-primary-foreground shadow-sm">
            <Flame className="size-5" />
          </span>
          <span className="leading-tight">
            <span className="font-heading text-xl font-bold tracking-tight text-stone-900">
              FoodEase
            </span>
            <span className="block text-[11px] font-medium uppercase tracking-[0.14em] text-muted-foreground">
              Pure Flavour & Tradition
            </span>
          </span>
        </Link>

        <nav className="hidden items-center gap-6 md:flex">
          <Link
            to="/"
            className="text-sm font-semibold text-stone-700 transition-colors hover:text-primary"
          >
            Home
          </Link>
          <Link
            to="/menu"
            className="text-sm font-semibold text-stone-700 transition-colors hover:text-primary"
          >
            Menu
          </Link>
        </nav>

        <form onSubmit={submit} className="hidden max-w-xs flex-1 lg:block">
          <label className="relative block">
            <SearchIcon />
            <input
              value={q}
              onChange={(e) => setQ(e.target.value)}
              placeholder="Search biryani, dosa, thali…"
              data-testid="navbar-search-input"
              className="w-full rounded-full border border-stone-200 bg-stone-50 py-2 pl-9 pr-3 text-sm outline-none transition-colors placeholder:text-stone-400 focus:border-primary focus:bg-white"
            />
          </label>
        </form>

        <Link
          to="/cart"
          data-testid="cart-nav-button"
          className="relative inline-flex items-center gap-2 rounded-full bg-primary px-4 py-2.5 text-sm font-bold text-primary-foreground shadow-sm transition-all hover:bg-primary/90 active:scale-95"
        >
          <ShoppingCart className="size-4" />
          <span className="hidden sm:inline">{count > 0 ? formatINR(subtotal) : "Cart"}</span>
          {count > 0 && (
            <span
              data-testid="cart-nav-count"
              className="absolute -right-1.5 -top-1.5 grid size-5 place-items-center rounded-full bg-secondary text-[11px] font-bold text-white ring-2 ring-white"
            >
              {count}
            </span>
          )}
        </Link>
      </div>
    </header>
  );
}

function SearchIcon() {
  return (
    <svg
      className="pointer-events-none absolute left-3 top-1/2 size-4 -translate-y-1/2 text-stone-400"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
    >
      <circle cx="11" cy="11" r="7" />
      <path d="m20 20-3.2-3.2" />
    </svg>
  );
}
