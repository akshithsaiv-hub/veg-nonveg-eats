import { Link, useParams } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { motion } from "motion/react";
import { Bike, ChefHat, CircleCheck, ReceiptText } from "lucide-react";
import { apiGet } from "@/lib/api";
import { VegBadge } from "@/components/VegBadge";
import { formatINR, type Order } from "@/lib/types";

const STAGES = [
  { key: "received", label: "Order Received", icon: ReceiptText },
  { key: "preparing", label: "Preparing in Kitchen", icon: ChefHat },
  { key: "out_for_delivery", label: "Out for Delivery", icon: Bike },
];

export default function OrderConfirmation() {
  const { orderId } = useParams<{ orderId: string }>();
  const { data: order, isLoading, isError } = useQuery({
    queryKey: ["order", orderId],
    queryFn: () => apiGet<Order>(`/orders/${orderId}`),
    retry: false,
    enabled: Boolean(orderId),
  });

  if (isError || (!isLoading && !order)) {
    return (
      <div className="mx-auto max-w-2xl px-4 py-24 text-center md:px-8">
        <p className="text-6xl">🧾</p>
        <h1 className="mt-4 font-heading text-3xl font-bold text-stone-900">
          We couldn't find that order
        </h1>
        <p className="mt-2 text-sm text-stone-500">
          The order may have been placed in another browser session.
        </p>
        <Link
          to="/menu"
          data-testid="order-notfound-cta"
          className="mt-7 inline-block rounded-full bg-primary px-7 py-3 text-sm font-bold text-white"
        >
          Back to Menu
        </Link>
      </div>
    );
  }

  if (isLoading || !order) {
    return (
      <div className="mx-auto max-w-2xl px-4 py-24 md:px-8">
        <div className="h-64 animate-pulse rounded-3xl bg-stone-100" />
      </div>
    );
  }

  return (
    <div className="mx-auto max-w-3xl px-4 py-12 md:px-8">
      <motion.div
        initial={{ opacity: 0, y: 16 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.4 }}
        className="overflow-hidden rounded-3xl border border-stone-200/80 bg-white shadow-sm"
      >
        <div className="bg-secondary px-8 py-8 text-center text-white">
          <motion.div
            initial={{ scale: 0 }}
            animate={{ scale: 1 }}
            transition={{ type: "spring", stiffness: 260, damping: 18, delay: 0.15 }}
            className="mx-auto grid size-16 place-items-center rounded-full bg-white/15"
          >
            <CircleCheck className="size-9" />
          </motion.div>
          <h1 className="mt-4 font-heading text-3xl font-bold">Order placed — thank you!</h1>
          <p className="mt-1 text-sm text-white/85">
            Your food is being prepared. Estimated arrival <strong>35–45 min</strong>.
          </p>
          <p
            data-testid="order-confirmation-id"
            className="mt-4 inline-block rounded-full bg-white/15 px-4 py-1.5 font-mono text-sm font-bold tracking-widest"
          >
            #{order.id}
          </p>
          <span
            data-testid="order-status-badge"
            className="ml-2 inline-block rounded-full bg-white px-4 py-1.5 text-xs font-bold uppercase tracking-wider text-secondary"
          >
            {order.status}
          </span>
        </div>

        {/* status tracker */}
        <div className="flex items-center justify-between gap-2 border-b border-stone-100 px-8 py-6">
          {STAGES.map((s, i) => (
            <div key={s.key} className="flex flex-1 flex-col items-center gap-1.5 text-center">
              <span
                className={`grid size-10 place-items-center rounded-full ${
                  i === 0 ? "bg-secondary text-white" : "bg-stone-100 text-stone-400"
                }`}
              >
                <s.icon className="size-4.5" />
              </span>
              <span
                className={`text-[11px] font-bold leading-tight ${
                  i === 0 ? "text-stone-900" : "text-stone-400"
                }`}
              >
                {s.label}
              </span>
              {i < STAGES.length - 1 && <span className="hidden" />}
            </div>
          ))}
        </div>

        <div className="px-8 py-6">
          <h2 className="font-heading text-xl font-bold text-stone-900">Items</h2>
          <ul className="mt-3 divide-y divide-stone-100" data-testid="order-items">
            {order.items.map((it) => (
              <li key={it.item_id} className="flex items-center justify-between gap-3 py-2.5 text-sm">
                <span className="flex min-w-0 items-center gap-2">
                  <VegBadge veg={it.veg} />
                  <span className="truncate font-semibold text-stone-800">{it.name}</span>
                  <span className="text-stone-400">× {it.quantity}</span>
                </span>
                <span className="font-mono font-bold text-stone-900">{formatINR(it.line_total)}</span>
              </li>
            ))}
          </ul>

          <dl className="mt-4 space-y-2 border-t border-dashed border-stone-200 pt-4 text-sm">
            <div className="flex justify-between">
              <dt className="text-stone-500">Subtotal</dt>
              <dd data-testid="order-subtotal-amount" className="font-mono font-bold">{formatINR(order.subtotal)}</dd>
            </div>
            <div className="flex justify-between">
              <dt className="text-stone-500">
                Discount {order.coupon_code ? <span className="font-mono">({order.coupon_code} {order.discount_percentage}%)</span> : null}
              </dt>
              <dd data-testid="order-discount-amount" className="font-mono font-bold text-green-600">
                −{formatINR(order.discount)}
              </dd>
            </div>
            <div className="flex justify-between">
              <dt className="text-stone-500">GST ({order.gst_percentage}%)</dt>
              <dd data-testid="order-gst-amount" className="font-mono font-bold">{formatINR(order.gst)}</dd>
            </div>
            <div className="flex justify-between border-t border-stone-200 pt-2.5">
              <dt className="text-base font-bold text-stone-900">Final Bill</dt>
              <dd data-testid="order-final-total" className="font-mono text-lg font-bold text-stone-900">
                {formatINR(order.total)}
              </dd>
            </div>
          </dl>

          <div className="mt-6 rounded-2xl bg-stone-50 p-4 text-sm text-stone-600">
            <span className="font-bold text-stone-800">{order.customer.name}</span> • {order.customer.phone}
            {order.customer.address ? <span> • {order.customer.address}</span> : null}
            <span className="mt-1 block text-xs text-stone-400">
              Payment: {order.customer.payment_mode === "cash" ? "Cash on Delivery" : order.customer.payment_mode.toUpperCase()}
            </span>
          </div>

          <div className="mt-7 flex flex-wrap gap-3">
            <Link
              to="/menu"
              data-testid="order-more-button"
              className="rounded-full bg-primary px-6 py-3 text-sm font-bold text-white transition-all hover:bg-primary/90 active:scale-95"
            >
              Order More
            </Link>
            <Link
              to="/"
              data-testid="order-home-button"
              className="rounded-full border border-stone-300 px-6 py-3 text-sm font-bold text-stone-700 transition-colors hover:border-primary hover:text-primary"
            >
              Back Home
            </Link>
          </div>
        </div>
      </motion.div>
    </div>
  );
}
