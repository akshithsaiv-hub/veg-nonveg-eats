// Cart data access — server cart identified by the `cart_id` httpOnly cookie.
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { apiDelete, apiGet, apiPatch, apiPost } from "@/lib/api";
import type { CartView } from "@/lib/types";

export function useCart() {
  return useQuery({ queryKey: ["cart"], queryFn: () => apiGet<CartView>("/cart") });
}

export function useAddToCart() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (v: { menu_item_id: string; quantity: number }) =>
      apiPost<CartView>("/cart/items", v),
    onSuccess: (data) => qc.setQueryData(["cart"], data),
  });
}

export function useUpdateCartItem() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (v: { menu_item_id: string; quantity: number }) =>
      apiPatch<CartView>(`/cart/items/${v.menu_item_id}`, { quantity: v.quantity }),
    onSuccess: (data) => qc.setQueryData(["cart"], data),
  });
}

export function useRemoveCartItem() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (menu_item_id: string) => apiDelete<CartView>(`/cart/items/${menu_item_id}`),
    onSuccess: (data) => qc.setQueryData(["cart"], data),
  });
}
