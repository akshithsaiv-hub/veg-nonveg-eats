// FSSAI-style dietary marker: green square + dot for veg, red square + dot for non-veg.
export function VegBadge({ veg, testId }: { veg: boolean; testId?: string }) {
  const color = veg ? "#15803D" : "#DC2626";
  return (
    <span
      data-testid={testId ?? (veg ? "veg-indicator" : "nonveg-indicator")}
      title={veg ? "Vegetarian" : "Non-vegetarian"}
      className="inline-grid size-4 shrink-0 place-items-center rounded-[3px] border-2 bg-white"
      style={{ borderColor: color }}
    >
      <span className="size-1.5 rounded-full" style={{ backgroundColor: color }} />
    </span>
  );
}
