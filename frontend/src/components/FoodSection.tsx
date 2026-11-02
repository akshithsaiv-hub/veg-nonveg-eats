import { motion } from "motion/react";
import FoodCard from "@/components/FoodCard";
import type { CategoryInfo, MenuItem } from "@/lib/types";

interface FoodSectionProps {
  category: CategoryInfo;
  items: MenuItem[];
  quantities: Record<string, number>; // menu item id -> quantity in cart
}

export default function FoodSection({ category, items, quantities }: FoodSectionProps) {
  if (items.length === 0) return null;
  return (
    <section id={category.id} data-testid={`category-section-${category.id}`} className="scroll-mt-32">
      <div className="mb-5 flex items-end justify-between gap-4">
        <div>
          <h2 className="font-heading text-2xl font-bold tracking-tight text-stone-900 md:text-3xl">
            <span className="mr-2">{category.emoji}</span>
            {category.name}
          </h2>
          <p className="mt-1 text-sm text-stone-500">
            {items.length} {items.length === 1 ? "dish" : "dishes"}
            {!category.veg && " • Non-vegetarian"}
          </p>
        </div>
        <span
          className={`hidden rounded-full px-3 py-1 text-xs font-bold sm:inline-block ${
            category.veg ? "bg-green-50 text-green-700" : "bg-red-50 text-red-600"
          }`}
        >
          {category.veg ? "🟢 Pure Veg" : "🔴 Non-Veg"}
        </span>
      </div>

      <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
        {items.map((item, i) => (
          <motion.div
            key={item.id}
            initial={{ opacity: 0, y: 14 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true, margin: "-40px" }}
            transition={{ duration: 0.35, delay: Math.min(i, 4) * 0.04 }}
          >
            <FoodCard item={item} quantity={quantities[item.id] ?? 0} />
          </motion.div>
        ))}
      </div>
    </section>
  );
}
