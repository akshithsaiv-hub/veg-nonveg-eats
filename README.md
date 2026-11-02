# 🍲 FoodEase — Authentic South Indian Veg & Non-Veg Food Ordering

A complete, production-ready, full-stack food ordering platform designed for authentic South Indian breakfast, lunch, thalis, biryanis, curries, and Chinese starters.

![FoodEase Preview](frontend/public/images/idly.jpg)

## 🌟 Features

- **Extensive Menu**: Over 104 curated dishes across 18 categories (Idli, Dosa, Combos, Biryanis, Andhra Meals, Seafood, Chinese Starters, Curries, and more).
- **Veg & Non-Veg Distinction**: Dedicated visual badges, category filtering, and instant toggle between pure vegetarian and non-vegetarian selections.
- **Interactive Shopping Cart**: Live cart counter, quantity increment/decrement, and instant subtotal recalculation.
- **Coupon System**: Discount coupons (`WELCOME10`, `COMBO15`, `FESTIVE20`) with dynamic validation and order thresholds.
- **GST & Pricing**: 5% GST calculation with Indian Rupee (₹) formatting and itemized billing breakdown.
- **Checkout & Order Tracking**: Smooth checkout flow with customer address, payment mode selection (UPI, Card, Cash on Delivery), order summary, and order confirmation receipt.
- **Java / Python Collections Demo**: Demonstrates collection structures (`ArrayList` -> `list[MenuItem]`, `HashMap` -> `dict[cart_id, qty]`, `HashSet` -> `set[categories]`, `ArrayList` -> `list[Order]`).

---

## 🛠️ Tech Stack

### Frontend
- **Framework**: React 19 + TypeScript
- **Bundler**: Vite
- **Styling**: Tailwind CSS 4 + Class Variance Authority
- **State & Data Fetching**: TanStack Query (React Query)
- **UI Components & Icons**: Radix / Base UI, Sonner (Toaster notifications), Lucide Icons
- **Routing**: React Router DOM 7

### Backend
- **Framework**: FastAPI (Python 3.10+)
- **Server**: Uvicorn
- **Validation**: Pydantic v2
- **Data Persistence**: JSON catalogs + In-memory Session & Cart state management

---

## 🚀 Getting Started

### Prerequisites
- Node.js (v18+) & npm
- Python (3.10+) & pip

### 1. Clone the repository
```bash
git clone https://github.com/akshithsaiv-hub/veg-nonveg-eats.git
cd veg-nonveg-eats
```

### 2. Backend Setup
```bash
cd backend
python -m venv venv
# Windows:
venv\Scripts\activate
# macOS/Linux:
source venv/bin/activate

pip install -r requirements.txt
python main.py
```
Backend runs on `http://localhost:8000`. API docs available at `http://localhost:8000/docs`.

### 3. Frontend Setup
In a new terminal:
```bash
cd frontend
npm install
npm run dev
```
Open your browser at `http://localhost:3000`.

---

## 📂 Project Structure

```
veg-nonveg-eats/
├── backend/
│   ├── data/
│   │   ├── menu.json         # 104 menu items with descriptions, prices, categories
│   │   ├── categories.json   # 18 food categories with emojis and seq
│   │   ├── coupons.json      # Promo coupon codes and discounts
│   │   └── config.json       # GST rate and settings
│   ├── main.py               # FastAPI application with REST endpoints
│   └── requirements.txt      # Python dependencies
├── frontend/
│   ├── public/
│   │   ├── images/           # 103 high-resolution food images
│   │   └── favicon.*         # Icons and branding
│   ├── src/
│   │   ├── components/       # FoodCard, FoodSection, Navbar, Footer, etc.
│   │   ├── hooks/            # useCart custom hook
│   │   ├── pages/            # Home, Menu, Cart, OrderConfirmation
│   │   ├── lib/              # API helpers, types, queryClient, utils
│   │   ├── App.tsx           # Routes & layout
│   │   └── main.tsx          # React entry point
│   ├── package.json
│   ├── vite.config.ts
│   └── tsconfig.json
├── .gitignore
└── README.md
```

---

## 📜 License
MIT License
