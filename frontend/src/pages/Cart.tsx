import { useState, type FormEvent } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { motion } from "motion/react";
import { toast } from "sonner";
import { ArrowLeft, Minus, Plus, Tag, Trash2, X } from "lucide-react";
import { ApiError, apiGet, apiPost } from "@/lib/api";
import { useAddToCart, useCart, useRemoveCartItem, useUpdateCartItem } from "@/hooks/useCart";
import { VegBadge } from "@/components/VegBadge";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { formatINR, type CouponInfo, type CouponValidateResponse } from "@/lib/types";

const PAY_LABELS: Record<string, string> = {
  cash: "Cash on Delivery",
  upi: "UPI",
  card: "Card",
};

export default function CartPage() {
  const { data: cart, isLoading } = useCart();
  const couponsQ = useQuery({
    queryKey: ["coupons"],
    queryFn: () => apiGet<CouponInfo[]>("/coupons"),
  });
  const configQ = useQuery({
    queryKey: ["config"],
    queryFn: () => apiGet<{ gst_rate: number }>("/collections/config"),
  });

  const updateItem = useUpdateCartItem();
  const removeItem = useRemoveCartItem();
  const addToCart = useAddToCart();
  const qc = useQueryClient();
  const navigate = useNavigate();

  const [couponInput, setCouponInput] = useState("");
  const [applied, setApplied] = useState<CouponInfo | null>(null);
  const [name, setName] = useState("");
  const [phone, setPhone] = useState("");
  const [address, setAddress] = useState("");
  const [payment, setPayment] = useState("cash");

  // Bill math mirrors lib/billing.py — the server recomputes authoritatively on order placement.
  const subtotal = cart?.subtotal ?? 0;
  const gstRate = configQ.data?.gst_rate ?? 5;
  const meetsMin = !applied || subtotal >= applied.min_order;
  const discount = applied && meetsMin ? Math.round(subtotal * applied.percentage) / 100 : 0;
  const gst = Math.round((subtotal - discount) * gstRate) / 100;
  const total = Math.round((subtotal - discount + gst) * 100) / 100;

  const validateCoupon = useMutation({
    mutationFn: (code: string) =>
      apiPost<CouponValidateResponse>("/coupons/validate", { code, subtotal }),
    onSuccess: (res) => {
      if (res.valid && res.code) {
        const info = (couponsQ.data ?? []).find((c) => c.code === res.code);
        setApplied(
          info ?? {
            code: res.code,
            percentage: res.percentage ?? 0,
            min_order: 0,
            label: res.message,
          },
        );
        setCouponInput(res.code);
        toast.success(res.message);
      } else {
        setApplied(null);
        toast.error(res.message);
      }
    },
    onError: () => toast.error("Could not validate the coupon — please try again"),
  });

  const placeOrder = useMutation({
    mutationFn: () =>
      apiPost<{ id: string }>("/orders", {
        customer: { name: name.trim(), phone: phone.replace(/\D/g, ""), address: address.trim(), payment_mode: payment },
        coupon_code: applied && meetsMin ? applied.code : null,
      }),
    onSuccess: (order) => {
      qc.setQueryData(["cart"], { cart_id: null, items: [], count: 0, subtotal: 0 });
      navigate(`/order/${order.id}`);
    },
    onError: (e) => {
      const detail =
        e instanceof ApiError && e.body && typeof e.body === "object" && "detail" in e.body
          ? String((e.body as { detail: unknown }).detail)
          : "Could not place the order — please try again";
      toast.error(detail);
    },
  });

  const submit = (e: FormEvent) => {
    e.preventDefault();
    if (!name.trim()) return toast.error("Please enter your name");
    if (!/^\d{10}$/.test(phone.replace(/\D/g, "")))
      return toast.error("Please enter a valid 10-digit phone number");
    placeOrder.mutate();
  };

  const apply = () => {
    if (!couponInput.trim()) return toast.error("Enter a coupon code first");
    validateCoupon.mutate(couponInput.trim());
  };

  if (!isLoading && (cart?.items.length ?? 0) === 0) {
    return (
      <div className="mx-auto max-w-2xl px-4 py-24 text-center md:px-8">
        <p className="text-6xl">🛒</p>
        <h1 className="mt-4 font-heading text-3xl font-bold tracking-tight text-stone-900">
          Your cart is craving something tasty
        </h1>
        <p className="mt-2 text-sm text-stone-500">
          Add idlis, biryanis or a thali from the menu — your bill and coupons wait here.
        </p>
        <Link
          to="/menu"
          data-testid="empty-cart-cta"
          className="mt-7 inline-block rounded-full bg-primary px-7 py-3 text-sm font-bold text-white shadow-sm transition-all hover:bg-primary/90 active:scale-95"
        >
          Browse the Menu
        </Link>
      </div>
    );
  }

  return (
    <div className="mx-auto max-w-7xl px-4 pb-24 pt-8 md:px-8">
      <Link
        to="/menu"
        className="inline-flex items-center gap-1.5 text-sm font-bold text-stone-500 transition-colors hover:text-primary"
      >
        <ArrowLeft className="size-4" /> Add more items
      </Link>
      <h1 className="mt-2 font-heading text-3xl font-bold tracking-tight text-stone-900 md:text-4xl">
        Your Cart {cart && cart.count > 0 && <span className="text-stone-400">({cart.count})</span>}
      </h1>

      <div className="mt-8 grid items-start gap-8 lg:grid-cols-[1fr_400px]">
        {/* ------- items + coupon ------- */}
        <div className="space-y-6">
          <div className="space-y-3">
            {isLoading
              ? Array.from({ length: 3 }).map((_, i) => (
                  <div key={i} className="h-28 animate-pulse rounded-2xl bg-stone-100" />
                ))
              : cart?.items.map((it) => (
                  <motion.div
                    key={it.id}
                    layout
                    data-testid={`cart-item-row-${it.id}`}
                    className="flex items-center gap-4 rounded-2xl border border-stone-200/80 bg-white p-3 shadow-sm"
                  >
                    <img
                      src={it.image}
                      alt={it.name}
                      className="size-20 rounded-xl object-cover"
                      loading="lazy"
                    />
                    <div className="min-w-0 flex-1">
                      <div className="flex items-center gap-2">
                        <VegBadge
                          veg={it.veg}
                          testId={it.veg ? `veg-indicator-${it.id}` : `nonveg-indicator-${it.id}`}
                        />
                        <h3 className="truncate text-sm font-bold text-stone-900">{it.name}</h3>
                      </div>
                      <p className="mt-0.5 text-xs text-stone-400">{formatINR(it.price)} each</p>
                      <div className="mt-2 inline-flex items-center gap-1 rounded-xl border border-stone-200 px-1 py-1">
                        <button
                          type="button"
                          onClick={() =>
                            updateItem.mutate({
                              menu_item_id: it.id,
                              quantity: Math.max(0, it.quantity - 1),
                            })
                          }
                          data-testid={`cart-qty-dec-${it.id}`}
                          className="grid size-6 place-items-center rounded-lg text-stone-600 hover:bg-stone-100"
                          aria-label={`Reduce ${it.name}`}
                        >
                          <Minus className="size-3.5" />
                        </button>
                        <span
                          data-testid={`cart-qty-count-${it.id}`}
                          className="min-w-7 text-center text-sm font-bold text-stone-900"
                        >
                          {it.quantity}
                        </span>
                        <button
                          type="button"
                          onClick={() => updateItem.mutate({ menu_item_id: it.id, quantity: it.quantity + 1 })}
                          data-testid={`cart-qty-inc-${it.id}`}
                          className="grid size-6 place-items-center rounded-lg text-stone-600 hover:bg-stone-100"
                          aria-label={`Add another ${it.name}`}
                        >
                          <Plus className="size-3.5" />
                        </button>
                      </div>
                    </div>
                    <div className="flex flex-col items-end gap-2">
                      <span className="font-mono text-base font-bold text-stone-900">
                        {formatINR(it.line_total)}
                      </span>
                      <button
                        type="button"
                        onClick={() => {
                          removeItem.mutate(it.id);
                          toast(`${it.name} removed`, {
                            action: {
                              label: "Undo",
                              onClick: () =>
                                addToCart.mutate({ menu_item_id: it.id, quantity: 1 }),
                            },
                          });
                        }}
                        data-testid={`cart-item-remove-${it.id}`}
                        className="inline-flex items-center gap-1 rounded-lg px-2 py-1 text-xs font-bold text-red-500 transition-colors hover:bg-red-50"
                      >
                        <Trash2 className="size-3.5" /> Remove
                      </button>
                    </div>
                  </motion.div>
                ))}
          </div>

          {/* coupon */}
          <div className="rounded-2xl border border-dashed border-amber-300 bg-orange-50/60 p-5">
            <h2 className="flex items-center gap-2 text-sm font-bold uppercase tracking-wider text-stone-700">
              <Tag className="size-4 text-primary" /> Discount coupon
            </h2>
            <div className="mt-3 flex gap-2">
              <input
                value={couponInput}
                onChange={(e) => setCouponInput(e.target.value.toUpperCase())}
                placeholder="e.g. WELCOME10"
                data-testid="coupon-input"
                className="w-full rounded-xl border border-stone-200 bg-white px-4 py-2.5 font-mono text-sm font-bold uppercase tracking-wider outline-none transition-colors placeholder:font-normal placeholder:tracking-normal placeholder:text-stone-400 focus:border-primary"
              />
              <button
                type="button"
                onClick={apply}
                disabled={validateCoupon.isPending}
                data-testid="apply-coupon-button"
                className="shrink-0 rounded-xl bg-secondary px-5 py-2.5 text-sm font-bold text-white transition-all hover:bg-secondary/90 active:scale-95 disabled:opacity-60"
              >
                Apply
              </button>
            </div>
            {applied && (
              <div
                data-testid="coupon-applied-note"
                className={`mt-3 flex items-center justify-between rounded-xl px-4 py-2.5 text-sm font-bold ${
                  meetsMin ? "bg-green-50 text-green-700" : "bg-amber-100 text-amber-800"
                }`}
              >
                <span className="font-mono">{applied.code}</span>
                <span>
                  {meetsMin
                    ? `${applied.percentage}% off applied — you save ${formatINR(discount)}`
                    : `Add ${formatINR(applied.min_order - subtotal)} more to unlock ${applied.code}`}
                </span>
                <button
                  type="button"
                  onClick={() => setApplied(null)}
                  aria-label="Remove coupon"
                  className="rounded-full p-1 hover:bg-black/5"
                >
                  <X className="size-4" />
                </button>
              </div>
            )}
            <div className="mt-3 flex flex-wrap gap-2">
              {(couponsQ.data ?? []).map((c) => (
                <button
                  key={c.code}
                  type="button"
                  data-testid={`coupon-chip-${c.code}`}
                  onClick={() => validateCoupon.mutate(c.code)}
                  className="rounded-full bg-white px-3 py-1.5 text-xs font-bold text-stone-700 ring-1 ring-stone-200 transition-all hover:ring-primary"
                >
                  <span className="font-mono text-primary">{c.code}</span> • {c.label}
                </button>
              ))}
            </div>
          </div>
        </div>

        {/* ------- bill + checkout ------- */}
        <form
          onSubmit={submit}
          className="space-y-5 lg:sticky lg:top-24"
          data-testid="checkout-form"
        >
          <div className="rounded-2xl border border-stone-200/80 bg-white p-6 shadow-sm">
            <h2 className="font-heading text-xl font-bold text-stone-900">Bill Summary</h2>
            <dl className="mt-4 space-y-2.5 text-sm">
              <div className="flex justify-between">
                <dt className="text-stone-500">Subtotal</dt>
                <dd data-testid="cart-subtotal-amount" className="font-mono font-bold text-stone-900">
                  {formatINR(subtotal)}
                </dd>
              </div>
              <div className="flex justify-between">
                <dt className="text-stone-500">Discount {applied && meetsMin ? `(${applied.percentage}%)` : ""}</dt>
                <dd data-testid="cart-discount-amount" className="font-mono font-bold text-green-600">
                  −{formatINR(discount)}
                </dd>
              </div>
              <div className="flex justify-between">
                <dt className="text-stone-500">GST ({gstRate}%)</dt>
                <dd data-testid="cart-gst-amount" className="font-mono font-bold text-stone-900">
                  {formatINR(gst)}
                </dd>
              </div>
              <div className="border-t border-dashed border-stone-200 pt-3">
                <div className="flex justify-between">
                  <dt className="text-base font-bold text-stone-900">Final Bill</dt>
                  <dd data-testid="cart-final-total" className="font-mono text-xl font-bold text-stone-900">
                    {formatINR(total)}
                  </dd>
                </div>
                <p className="mt-1 text-right text-[11px] text-stone-400">
                  Subtotal − Discount + GST
                </p>
              </div>
            </dl>
          </div>

          <div className="rounded-2xl border border-stone-200/80 bg-white p-6 shadow-sm">
            <h2 className="font-heading text-xl font-bold text-stone-900">Delivery details</h2>
            <div className="mt-4 space-y-3">
              <input
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="Your name"
                data-testid="checkout-name-input"
                className="w-full rounded-xl border border-stone-200 px-4 py-2.5 text-sm outline-none transition-colors placeholder:text-stone-400 focus:border-primary"
              />
              <input
                value={phone}
                onChange={(e) => setPhone(e.target.value)}
                placeholder="10-digit phone number"
                inputMode="numeric"
                data-testid="checkout-phone-input"
                className="w-full rounded-xl border border-stone-200 px-4 py-2.5 text-sm outline-none transition-colors placeholder:text-stone-400 focus:border-primary"
              />
              <textarea
                value={address}
                onChange={(e) => setAddress(e.target.value)}
                placeholder="Delivery address (optional)"
                rows={2}
                data-testid="checkout-address-input"
                className="w-full resize-none rounded-xl border border-stone-200 px-4 py-2.5 text-sm outline-none transition-colors placeholder:text-stone-400 focus:border-primary"
              />
              <Select value={payment} onValueChange={(v: string) => setPayment(v)}>
                <SelectTrigger data-testid="payment-mode-select" className="w-full">
                  <SelectValue>{(v: string) => PAY_LABELS[v] ?? v}</SelectValue>
                </SelectTrigger>
                <SelectContent>
                  {Object.entries(PAY_LABELS).map(([value, label]) => (
                    <SelectItem key={value} value={value}>
                      {label}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              <button
                type="submit"
                disabled={placeOrder.isPending}
                data-testid="place-order-button"
                className="w-full rounded-xl bg-primary py-3.5 text-sm font-bold text-white shadow-sm transition-all hover:bg-primary/90 active:scale-[0.98] disabled:opacity-60"
              >
                {placeOrder.isPending ? "Placing order…" : `Pay ${formatINR(total)} • Place Order`}
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
}
