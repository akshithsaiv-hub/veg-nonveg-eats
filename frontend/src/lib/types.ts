// Hand-written mirrors of the Pydantic models in backend/models/menu.py.
// Nothing infers across the HTTP boundary — keep this file in sync with the models in the same edit.

export interface MenuItem {
  id: string;
  name: string;
  description: string;
  price: number; // whole rupees (₹)
  veg: boolean;
  category: string;
  image: string;
  best_seller: boolean;
  popular: boolean;
  seq: number;
}

export interface CategoryInfo {
  id: string;
  name: string;
  emoji: string;
  veg: boolean;
  seq: number;
  item_count: number;
}

export interface CartItemView {
  id: string;
  name: string;
  price: number;
  veg: boolean;
  image: string;
  quantity: number;
  line_total: number;
}

export interface CartView {
  cart_id: string | null;
  items: CartItemView[];
  count: number;
  subtotal: number;
}

export interface CouponInfo {
  code: string;
  percentage: number;
  min_order: number;
  label: string;
}

export interface CouponValidateResponse {
  valid: boolean;
  message: string;
  code?: string | null;
  percentage?: number | null;
  discount?: number | null;
}

export interface CustomerInfo {
  name: string;
  phone: string;
  address: string;
  payment_mode: string; // cash | upi | card
}

export interface OrderItemView {
  item_id: string;
  name: string;
  price: number;
  veg: boolean;
  quantity: number;
  line_total: number;
}

export interface Order {
  id: string;
  items: OrderItemView[];
  customer: CustomerInfo;
  subtotal: number;
  discount_percentage: number;
  discount: number;
  gst_percentage: number;
  gst: number;
  total: number;
  coupon_code: string | null;
  status: string;
  created_at: string;
}

export interface FeaturedMenu {
  best_sellers: MenuItem[];
  popular: MenuItem[];
}

export interface CollectionsStats {
  menu_items_structure: string;
  menu_items_count: number;
  cart_structure: string;
  active_carts_count: number;
  categories_structure: string;
  unique_categories_count: number;
  unique_categories: string[];
  orders_structure: string;
  orders_history_count: number;
}

// ₹ formatting with Indian digit grouping (e.g. 12,340.5)
export function formatINR(value: number): string {
  return `₹${value.toLocaleString("en-IN", { maximumFractionDigits: 2 })}`;
}
