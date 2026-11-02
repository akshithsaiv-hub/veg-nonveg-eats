import { Minus, Plus, Star } from "lucide-react";
import { motion } from "motion/react";
import { toast } from "sonner";
import { useAddToCart, useUpdateCartItem } from "@/hooks/useCart";
import { VegBadge } from "@/components/VegBadge";
import { formatINR, type MenuItem } from "@/lib/types";

interface FoodCardProps {
  item: MenuItem;
  quantity: number; // current quantity in cart (0 if absent)
}

export default function FoodCard({ item, quantity }: FoodCardProps) {
  const addToCart = useAddToCart();
  const updateItem = useUpdateCartItem();

  const add = () => {
    addToCart.mutate(
      { menu_item_id: item.id, quantity: 1 },
      { onSuccess: () => toast.success(`${item.name} added to cart`) },
    );
  };
  const inc = () => {
    addToCart.mutate({ menu_item_id: item.id, quantity: 1 });
  };
  const dec = () => {
    updateItem.mutate({ menu_item_id: item.id, quantity: Math.max(0, quantity - 1) });
  };

  return (
    <article
      data-testid={`food-card-${item.id}`}
      className="group flex flex-col overflow-hidden rounded-2xl border border-stone-200/80 bg-white shadow-sm transition-all duration-300 hover:-translate-y-0.5 hover:border-amber-300 hover:shadow-md"
    >
      <div className="relative h-44 overflow-hidden bg-stone-100">
        <img
          src={item.image}
          alt={item.name}
          loading="lazy"
          decoding="async"
          data-testid={`food-card-image-${item.id}`}
          className="h-full w-full object-cover transition-transform duration-500 group-hover:scale-105"
        />
        {item.best_seller && (
          <span className="absolute left-3 top-3 inline-flex items-center gap-1 rounded-full bg-amber-500/95 px-2 py-0.5 text-[11px] font-bold text-white shadow-sm">
            <Star className="size-3 fill-white" /> Best Seller
          </span>
        )}
        <span
          data-testid={`veg-indicator-${item.id}`}
          className="absolute bottom-3 left-3"
        >
          <VegBadge veg={item.veg} />
        </span>
        <span className="absolute bottom-3 right-3 rounded-lg bg-white/95 px-2.5 py-1 font-mono text-sm font-bold text-stone-900 shadow-sm">
          {formatINR(item.price)}
        </span>
      </div>

      <div className="flex flex-1 flex-col gap-1.5 p-4">
        <div className="flex items-start justify-between gap-2">
          <h3
            data-testid={`food-card-title-${item.id}`}
            className="text-base font-bold leading-snug text-stone-900"
          >
            {item.name}
          </h3>
        </div>
        <p className="line-clamp-2 text-[13px] leading-relaxed text-stone-500">
          {item.description}
        </p>

        <div className="mt-auto flex items-center justify-between gap-2 pt-3">
          <div className="flex flex-col">
            <span className="text-[11px] font-medium uppercase tracking-wider text-stone-400">
              Price
            </span>
            <span
              data-testid={`food-card-price-${item.id}`}
              className="font-mono text-lg font-bold text-stone-900"
            >
              {formatINR(item.price)}
            </span>
          </div>

          {quantity === 0 ? (
            <button
              type="button"
              onClick={add}
              disabled={addToCart.isPending}
              data-testid={`food-card-add-btn-${item.id}`}
              className="inline-flex items-center gap-1 rounded-xl bg-primary px-4 py-2 text-sm font-bold text-primary-foreground shadow-sm transition-all hover:bg-primary/90 active:scale-95 disabled:opacity-60"
            >
              <Plus className="size-4" /> ADD
            </button>
          ) : (
            <motion.div
              key={quantity}
              initial={{ scale: 1.08 }}
              animate={{ scale: 1 }}
              data-testid={`food-card-stepper-${item.id}`}
              className="inline-flex items-center gap-1 rounded-xl bg-primary px-1 py-1 text-primary-foreground shadow-sm"
            >
              <button
                type="button"
                onClick={dec}
                data-testid={`food-card-qty-dec-${item.id}`}
                className="grid size-7 place-items-center rounded-lg transition-colors hover:bg-white/20"
                aria-label={`Reduce ${item.name}`}
              >
                <Minus className="size-4" />
              </button>
              <span
                data-testid={`food-card-qty-count-${item.id}`}
                className="min-w-6 text-center text-sm font-bold"
              >
                {quantity}
              </span>
              <button
                type="button"
                onClick={inc}
                data-testid={`food-card-qty-inc-${item.id}`}
                className="grid size-7 place-items-center rounded-lg transition-colors hover:bg-white/20"
                aria-label={`Add another ${item.name}`}
              >
                <Plus className="size-4" />
              </button>
            </motion.div>
          )}
        </div>
      </div>
    </article>
  );
}
