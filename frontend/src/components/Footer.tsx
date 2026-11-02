import { Link } from "react-router-dom";
import { Clock, Flame, MapPin, Phone } from "lucide-react";

export default function Footer() {
  return (
    <footer className="mt-20 bg-stone-900 text-stone-300">
      <div className="mx-auto grid max-w-7xl gap-10 px-4 py-12 md:grid-cols-3 md:px-8">
        <div>
          <div className="flex items-center gap-2.5">
            <span className="grid size-10 place-items-center rounded-xl bg-primary text-white">
              <Flame className="size-5" />
            </span>
            <span>
              <span className="font-heading text-xl font-bold text-white">FoodEase</span>
              <span className="block text-[11px] uppercase tracking-[0.14em] text-stone-400">
                Pure Flavour & Tradition
              </span>
            </span>
          </div>
          <p className="mt-4 max-w-sm text-sm leading-relaxed text-stone-400">
            Authentic South Indian tiffins, biryanis and Indo-Chinese favourites — plus a
            full non-veg kitchen. Cooked fresh, delivered hot.
          </p>
        </div>

        <div>
          <h3 className="text-sm font-bold uppercase tracking-wider text-stone-100">Explore</h3>
          <ul className="mt-4 space-y-2 text-sm">
            <li><Link to="/" className="transition-colors hover:text-orange-400">Home</Link></li>
            <li><Link to="/menu" className="transition-colors hover:text-orange-400">Full Menu</Link></li>
            <li><Link to="/menu#best-sellers" className="transition-colors hover:text-orange-400">⭐ Best Sellers</Link></li>
            <li><Link to="/menu#chicken-biryani" className="transition-colors hover:text-orange-400">🍗 Chicken Biryani</Link></li>
            <li><Link to="/menu#thali" className="transition-colors hover:text-orange-400">🍛 Thali & Meals</Link></li>
            <li><Link to="/cart" className="transition-colors hover:text-orange-400">Your Cart</Link></li>
          </ul>
        </div>

        <div>
          <h3 className="text-sm font-bold uppercase tracking-wider text-stone-100">Reach Us</h3>
          <ul className="mt-4 space-y-3 text-sm text-stone-400">
            <li className="flex items-start gap-2"><MapPin className="mt-0.5 size-4 shrink-0 text-orange-400" /> 12, MG Road, Bengaluru — 560001</li>
            <li className="flex items-center gap-2"><Phone className="size-4 shrink-0 text-orange-400" /> +91 98765 43210</li>
            <li className="flex items-center gap-2"><Clock className="size-4 shrink-0 text-orange-400" /> Open daily, 7:00 AM – 10:30 PM</li>
          </ul>
        </div>
      </div>
      <div className="border-t border-white/10 py-5 text-center text-xs text-stone-500">
        © 2026 FoodEase • Academic demonstration — discount coupons & 5% GST applied at checkout • All prices in ₹
      </div>
    </footer>
  );
}
