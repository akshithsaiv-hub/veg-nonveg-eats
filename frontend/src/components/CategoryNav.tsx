import type { CategoryInfo } from "@/lib/types";

interface CategoryNavProps {
  categories: CategoryInfo[];
  activeId: string;
}

// Sticky horizontal pill bar — clicking a pill smooth-jumps to its menu section.
export default function CategoryNav({ categories, activeId }: CategoryNavProps) {
  return (
    <div
      data-testid="category-nav-bar"
      className="sticky top-16 z-30 border-b border-stone-200 bg-[#FAF8F5]/95 backdrop-blur-sm"
    >
      <div className="no-scrollbar mx-auto flex max-w-7xl items-center gap-2 overflow-x-auto px-4 py-2.5 md:px-8">
        {categories.map((c) => (
          <a
            key={c.id}
            href={`#${c.id}`}
            data-testid={`category-tab-${c.id}`}
            className={`whitespace-nowrap rounded-full px-3.5 py-1.5 text-[13px] font-semibold transition-colors ${
              activeId === c.id
                ? "bg-secondary text-white shadow-sm"
                : "bg-stone-100 text-stone-700 hover:bg-stone-200"
            }`}
          >
            <span className="mr-1">{c.emoji}</span>
            {c.name}
            <span className={`ml-1.5 text-[11px] ${activeId === c.id ? "text-white/70" : "text-stone-400"}`}>
              {c.item_count}
            </span>
          </a>
        ))}
      </div>
    </div>
  );
}
