import os
import json
import uuid
from datetime import datetime, timezone
from typing import Optional, List, Dict
from fastapi import FastAPI, HTTPException, Request, Response, status
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel

app = FastAPI(title="FoodEase API", description="Veg & Non-Veg Food Ordering Backend")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
DATA_DIR = os.path.join(BASE_DIR, "data")

with open(os.path.join(DATA_DIR, "menu.json"), "r", encoding="utf-8") as f:
    MENU_ITEMS = json.load(f)

with open(os.path.join(DATA_DIR, "categories.json"), "r", encoding="utf-8") as f:
    CATEGORIES = json.load(f)

with open(os.path.join(DATA_DIR, "coupons.json"), "r", encoding="utf-8") as f:
    COUPONS = json.load(f)

with open(os.path.join(DATA_DIR, "config.json"), "r", encoding="utf-8") as f:
    CONFIG = json.load(f)

# Fast lookup for menu items
MENU_MAP = {item["id"]: item for item in MENU_ITEMS}

# In-memory Cart Storage: cart_id -> dict of {item_id: quantity}
CARTS: Dict[str, Dict[str, int]] = {}

# In-memory Orders Storage: order_id -> order dict
ORDERS: Dict[str, dict] = {}


# --- Pydantic Schemas ---
class AddCartItemRequest(BaseModel):
    menu_item_id: str
    quantity: int = 1

class UpdateCartItemRequest(BaseModel):
    quantity: int

class CouponValidateRequest(BaseModel):
    code: str
    subtotal: float

class CustomerInfo(BaseModel):
    name: str
    phone: str
    address: str
    payment_mode: str

class OrderCreateRequest(BaseModel):
    customer: CustomerInfo
    coupon_code: Optional[str] = None


# --- Helper Functions ---
def get_or_create_cart_id(request: Request, response: Response) -> str:
    cart_id = request.cookies.get("cart_id")
    if not cart_id or cart_id not in CARTS:
        cart_id = uuid.uuid4().hex
        CARTS[cart_id] = {}
        response.set_cookie(key="cart_id", value=cart_id, httponly=True, samesite="lax")
    return cart_id

def format_cart_view(cart_id: str) -> dict:
    items_dict = CARTS.get(cart_id, {})
    cart_items = []
    subtotal = 0
    count = 0

    for item_id, qty in items_dict.items():
        if item_id in MENU_MAP and qty > 0:
            menu_item = MENU_MAP[item_id]
            price = menu_item["price"]
            line_total = price * qty
            cart_items.append({
                "id": item_id,
                "name": menu_item["name"],
                "price": price,
                "veg": menu_item["veg"],
                "image": menu_item["image"],
                "quantity": qty,
                "line_total": line_total
            })
            subtotal += line_total
            count += qty

    return {
        "cart_id": cart_id,
        "items": cart_items,
        "count": count,
        "subtotal": subtotal
    }


# --- API Routes ---
@app.get("/api/menu")
def get_menu():
    return MENU_ITEMS

@app.get("/api/menu/categories")
def get_categories():
    return CATEGORIES

@app.get("/api/menu/featured")
def get_featured():
    best_sellers = [item for item in MENU_ITEMS if item.get("best_seller")]
    popular = [item for item in MENU_ITEMS if item.get("popular")]
    return {
        "best_sellers": best_sellers,
        "popular": popular
    }

@app.get("/api/coupons")
def get_coupons():
    return COUPONS

@app.post("/api/coupons/validate")
def validate_coupon(payload: CouponValidateRequest):
    code_upper = payload.code.strip().upper()
    found = next((c for c in COUPONS if c["code"].upper() == code_upper), None)
    if not found:
        return {
            "valid": False,
            "message": "Invalid coupon code"
        }
    
    if payload.subtotal < found["min_order"]:
        return {
            "valid": False,
            "message": f"Coupon valid on orders above ₹{found['min_order']}"
        }
    
    discount = round(payload.subtotal * (found["percentage"] / 100.0), 2)
    return {
        "valid": True,
        "message": f"{found['code']} applied — {int(found['percentage'])}% off",
        "code": found["code"],
        "percentage": found["percentage"],
        "discount": discount
    }

@app.get("/api/collections/config")
def get_config():
    return CONFIG

@app.get("/api/collections/stats")
def get_collections_stats():
    unique_cats = sorted(list({c["id"] for c in CATEGORIES}))
    return {
        "menu_items_structure": "ArrayList<FoodItem> -> python list[MenuItem]",
        "menu_items_count": len(MENU_ITEMS),
        "cart_structure": "HashMap<String,Integer> -> python dict[menu_item_id, qty]",
        "active_carts_count": len(CARTS),
        "categories_structure": "HashSet<String> -> python set[str]",
        "unique_categories_count": len(unique_cats),
        "unique_categories": unique_cats,
        "orders_structure": "ArrayList<Order> -> python list[Order]",
        "orders_history_count": len(ORDERS)
    }

@app.get("/api/cart")
def get_cart(request: Request, response: Response):
    cart_id = request.cookies.get("cart_id")
    if not cart_id or cart_id not in CARTS:
        return {"cart_id": None, "items": [], "count": 0, "subtotal": 0}
    return format_cart_view(cart_id)

@app.post("/api/cart/items")
def add_to_cart(payload: AddCartItemRequest, request: Request, response: Response):
    if payload.menu_item_id not in MENU_MAP:
        raise HTTPException(status_code=404, detail="Item not found")
    cart_id = get_or_create_cart_id(request, response)
    current_qty = CARTS[cart_id].get(payload.menu_item_id, 0)
    CARTS[cart_id][payload.menu_item_id] = current_qty + payload.quantity
    return format_cart_view(cart_id)

@app.patch("/api/cart/items/{item_id}")
def update_cart_item(item_id: str, payload: UpdateCartItemRequest, request: Request, response: Response):
    cart_id = get_or_create_cart_id(request, response)
    if payload.quantity <= 0:
        CARTS[cart_id].pop(item_id, None)
    else:
        CARTS[cart_id][item_id] = payload.quantity
    return format_cart_view(cart_id)

@app.delete("/api/cart/items/{item_id}")
def delete_cart_item(item_id: str, request: Request, response: Response):
    cart_id = get_or_create_cart_id(request, response)
    CARTS[cart_id].pop(item_id, None)
    return format_cart_view(cart_id)

@app.post("/api/orders")
def place_order(payload: OrderCreateRequest, request: Request, response: Response):
    cart_id = request.cookies.get("cart_id")
    if not cart_id or cart_id not in CARTS or not CARTS[cart_id]:
        raise HTTPException(status_code=400, detail="Cart is empty")

    cart_view = format_cart_view(cart_id)
    if not cart_view["items"]:
        raise HTTPException(status_code=400, detail="Cart is empty")

    subtotal = float(cart_view["subtotal"])
    discount_percentage = 0.0
    discount = 0.0

    if payload.coupon_code:
        code_upper = payload.coupon_code.strip().upper()
        found = next((c for c in COUPONS if c["code"].upper() == code_upper), None)
        if found and subtotal >= found["min_order"]:
            discount_percentage = float(found["percentage"])
            discount = round(subtotal * (discount_percentage / 100.0), 2)

    gst_percentage = float(CONFIG.get("gst_rate", 5.0))
    taxable = max(0.0, subtotal - discount)
    gst = round(taxable * (gst_percentage / 100.0), 2)
    total = round(taxable + gst, 2)

    order_id = uuid.uuid4().hex[:12].upper()
    order_items = [
        {
            "item_id": it["id"],
            "name": it["name"],
            "price": it["price"],
            "veg": it["veg"],
            "quantity": it["quantity"],
            "line_total": it["line_total"]
        }
        for it in cart_view["items"]
    ]

    order = {
        "id": order_id,
        "items": order_items,
        "customer": payload.customer.model_dump(),
        "subtotal": subtotal,
        "discount_percentage": discount_percentage,
        "discount": discount,
        "gst_percentage": gst_percentage,
        "gst": gst,
        "total": total,
        "coupon_code": payload.coupon_code,
        "status": "received",
        "created_at": datetime.now(timezone.utc).isoformat()
    }

    ORDERS[order_id] = order
    # Clear cart after successful order
    CARTS[cart_id] = {}

    return order

@app.get("/api/orders/{order_id}")
def get_order(order_id: str):
    if order_id not in ORDERS:
        raise HTTPException(status_code=404, detail=f"Order {order_id} not found")
    return ORDERS[order_id]

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
