import { Link, useLocation } from "react-router-dom";
import { ShoppingCart } from "lucide-react";
import { useCart } from "@/hooks/useCart";
import { formatINR } from "@/lib/types";

// Sticky bottom cart bar on small screens (hidden on the cart page itself).
export default function MobileCartBar() {
  const { data: cart } = useCart();
  const { pathname } = useLocation();
  if (!cart || cart.count === 0 || pathname === "/cart") return null;
  return (
    <Link
      to="/cart"
      data-testid="mobile-cart-bar"
      className="fixed inset-x-4 bottom-4 z-40 flex items-center justify-between rounded-2xl bg-secondary px-5 py-3.5 text-white shadow-lg sm:hidden"
    >
      <span className="text-sm font-semibold">
        {cart.count} {cart.count === 1 ? "item" : "items"} • {formatINR(cart.subtotal)}
      </span>
      <span className="inline-flex items-center gap-1.5 text-sm font-bold">
        <ShoppingCart className="size-4" /> View Cart
      </span>
    </Link>
  );
}
