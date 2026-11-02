# 🍲 FoodEase — South Indian Veg & Non-Veg Food Ordering Platform

[![Java 21](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/technologies/downloads/#java21)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React 19](https://img.shields.io/badge/React-19.2-blue.svg)](https://react.dev/)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind%20CSS-4.0-38B2AC.svg)](https://tailwindcss.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**FoodEase** is a production-ready, full-stack food ordering platform designed for authentic South Indian breakfast, lunch, thalis, biryanis, curries, and Chinese starters.

Originally designed with a lightweight prototype backend, the system has been transformed into a **robust, enterprise-grade Java 21 + Spring Boot 3 backend** built around **Object-Oriented Programming (OOP)** and **SOLID software engineering principles**.

---

## 🌟 Key Features

* **Extensive Menu Catalog**: Over 104 curated dishes across 18 categories (Idli, Dosa, Combos, Biryanis, Andhra Meals, Seafood, Chinese Starters, Curries, and more).
* **Pure Veg & Non-Veg Distinction**: Dedicated visual badges, category filtering, and instant toggle between pure vegetarian and non-vegetarian selections.
* **Interactive Shopping Cart**: Live cart counter, quantity increment/decrement, and instant subtotal recalculation using session cookies.
* **Coupon System**: Discount coupons (`WELCOME10`, `COMBO15`, `FESTIVE20`) with dynamic validation and order thresholds.
* **GST & Pricing**: 5% GST calculation with Indian Rupee (₹) formatting and itemized billing breakdown.
* **Checkout & Order Tracking**: Smooth checkout flow with customer address, payment mode selection (UPI, Card, Cash on Delivery), order summary, and order confirmation receipt.
* **Complete OOP Design**: Implements Encapsulation, Inheritance, Polymorphism (Overriding & Overloading), Abstraction, Composition, Aggregation, and all 5 SOLID principles.
* **Zero-Setup Embedded Database**: Pre-configured with H2 in-memory JPA database with embedded web console, plus seamless switch to MySQL.

---

## 🏗️ Architecture & Tech Stack

```
   ┌─────────────────────────────────────────────────────────┐
   │             React 19 Frontend (Vite + Tailwind)         │
   └────────────────────────────┬────────────────────────────┘
                                │  HTTP REST /api/*
                                ▼
   ┌─────────────────────────────────────────────────────────┐
   │        Spring Boot 3.3.5 REST Controllers (Port 8000)   │
   ├─────────────────────────────────────────────────────────┤
   │        Service Layer (Interfaces & Business Logic)      │
   ├─────────────────────────────────────────────────────────┤
   │        Strategy Layer (Discount & Payment Processors)   │
   ├─────────────────────────────────────────────────────────┤
   │        Domain Entities & Encapsulated Models            │
   ├─────────────────────────────────────────────────────────┤
   │        Spring Data JPA & Hibernate 6 Repositories       │
   ├─────────────────────────────────────────────────────────┤
   │        Database: H2 (Default) / MySQL (Configurable)    │
   └─────────────────────────────────────────────────────────┘
```

### Frontend
- **Framework**: React 19 + TypeScript
- **Bundler**: Vite
- **Styling**: Tailwind CSS 4 + Class Variance Authority
- **State & Data Fetching**: TanStack Query (React Query)
- **UI Components & Icons**: Radix / Base UI, Sonner (Toaster notifications), Lucide Icons
- **Routing**: React Router DOM 7

### Backend
- **Language**: Java 21 LTS
- **Framework**: Spring Boot 3.3.5
- **Data Access**: Spring Data JPA, Hibernate 6
- **Database**: H2 (In-memory, default for zero-setup execution) / MySQL (production ready)
- **Validation**: Jakarta Validation (Hibernate Validator)
- **Build Tool**: Apache Maven 3.9+ / Maven Wrapper (`mvnw`)

---

## 🚀 Quick Start Guide

### Prerequisites
- **Java 21 LTS** installed (`java -version`)
- **Node.js (v18+) & npm** (`node -v`, `npm -v`)
- Maven is included via the **Maven Wrapper** (`mvnw` / `mvnw.cmd`).

---

### Step 1: Clone the Repository
```bash
git clone https://github.com/akshithsaiv-hub/veg-nonveg-eats.git
cd veg-nonveg-eats
```

---

### Step 2: Start the Java Spring Boot Backend

#### On Windows:
```cmd
cd backend
mvnw.cmd spring-boot:run
```
*(Or use `java -jar target/foodease-backend-1.0.0.jar`)*

#### On Linux / macOS:
```bash
cd backend
./mvnw spring-boot:run
```

The Spring Boot backend will start on **`http://localhost:8000`**.  
On startup, `DataInitializer.java` automatically seeds:
- 18 Food Categories
- 104 Authentic Menu Items
- 3 Discount Coupons (`WELCOME10`, `COMBO15`, `FESTIVE20`)
- Default Administrator User

> **H2 Database Console:**  
> Visit `http://localhost:8000/h2-console`  
> JDBC URL: `jdbc:h2:mem:foodeasedb` | Username: `sa` | Password: *(blank)*

---

### Step 3: Start the React Frontend

Open a new terminal window:
```bash
cd frontend
npm install
npm run dev
```

Open your browser at **`http://localhost:3000`** to browse, add items to cart, apply discount coupons, and place orders!

---

## 🗄️ Database Configuration

By default, FoodEase runs on **H2 In-Memory Database** so that the project can be cloned, compiled, and evaluated with **zero database configuration**.

To switch to **MySQL**:
1. Open [`backend/src/main/resources/application.properties`](backend/src/main/resources/application.properties)
2. Comment out the H2 settings and enable MySQL:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/foodeasedb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

---

## 📐 Object-Oriented Programming (OOP) Implementation

For detailed academic evaluation, please refer to the dedicated guide:  
👉 **[OOP_IMPLEMENTATION.md](OOP_IMPLEMENTATION.md)**

### Key Highlights:
1. **Encapsulation**: Domain models (`MenuItem`, `Cart`, `Order`, `Coupon`, `Customer`) protect internal state with private attributes, input validation, and business domain methods.
2. **Inheritance**: Clean class hierarchies:
   - `BaseEntity` (`@MappedSuperclass`) provides audit timestamps for all persistent models.
   - `User` abstract class extended by `Customer` and `Admin`.
   - `AbstractDiscountStrategy` extended by `PercentageDiscountStrategy` and `ThresholdDiscountStrategy`.
3. **Polymorphism**:
   - **Method Overriding**: Polymorphic discount calculations (`DiscountStrategy`), payment gateway dispatching (`PaymentProcessor`), and role-based portal access (`getDashboardAccess()`).
   - **Method Overloading**: Parameterized financial calculations in `OrderService` (`calculateTotal`), cart item management (`addItem`), and menu filtering queries (`getMenuItems`).
4. **Abstraction**: Complete decoupling of layers through interfaces (`MenuService`, `CartService`, `OrderService`, `CouponService`, `DiscountStrategy`, `PaymentProcessor`).
5. **Composition & Association**:
   - **Composition**: `Order` has `OrderItem`s; `Cart` has `CartItem`s (cascading lifecycle).
   - **Association**: `OrderItem` references `MenuItem`; `Customer` places `Order`s.
6. **SOLID Principles**: Comprehensive adherence to SRP, OCP, LSP, ISP, and DIP throughout the architecture.

---

## 📡 REST API Documentation

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/menu` | Retrieve all 104 menu items (filters: `?category=`, `?veg=`) |
| `GET` | `/api/menu/categories` | Retrieve all 18 food categories with emojis and counts |
| `GET` | `/api/menu/featured` | Retrieve best sellers and popular recommendations |
| `GET` | `/api/menu/{id}` | Retrieve details for a single menu item |
| `GET` | `/api/coupons` | List all active promotional coupons |
| `POST` | `/api/coupons/validate` | Validate coupon code against cart subtotal |
| `GET` | `/api/collections/config` | GST rate (5%) and promo coupon settings |
| `GET` | `/api/collections/stats` | Academic demonstration of Collections & JPA entities |
| `GET` | `/api/cart` | Retrieve current user's session shopping cart |
| `POST` | `/api/cart/items` | Add item to cart (`menu_item_id`, `quantity`) |
| `PATCH` | `/api/cart/items/{itemId}` | Update quantity of a cart item |
| `DELETE` | `/api/cart/items/{itemId}` | Remove item from cart |
| `POST` | `/api/orders` | Place order with customer delivery address & payment mode |
| `GET` | `/api/orders/{orderId}` | Fetch itemized order confirmation receipt |
| `GET` | `/api/users` | List users demonstrating polymorphic dashboard permissions |

---

## 📂 Project Directory Structure

```
veg-nonveg-eats/
├── backend/                                   # Java 21 + Spring Boot Backend
│   ├── src/main/java/com/foodease/
│   │   ├── FoodEaseApplication.java           # Spring Boot Entry Point
│   │   ├── config/                            # CORS, DataInitializer (Startup Seeder)
│   │   ├── controller/                        # REST Controllers
│   │   ├── dto/                               # Request/Response Data Transfer Objects
│   │   ├── enums/                             # OrderStatus, PaymentMode, UserRole
│   │   ├── exception/                         # GlobalExceptionHandler, Custom Exceptions
│   │   ├── model/                             # JPA Entities (User, Customer, Order, etc.)
│   │   ├── repository/                        # Spring Data JPA Repositories
│   │   ├── service/                           # Interfaces & Implementations
│   │   │   └── strategy/                      # Discount & Payment Strategy Patterns
│   │   └── util/                              # OrderIdGenerator, CurrencyFormatter
│   ├── src/main/resources/
│   │   ├── application.properties             # Port 8000, H2 & MySQL configuration
│   │   └── data/                              # JSON seed files (menu, categories, coupons)
│   ├── pom.xml                                # Maven Dependencies & Build Configuration
│   ├── mvnw & mvnw.cmd                        # Maven Wrapper executables
├── frontend/                                  # React 19 + TypeScript Frontend
│   ├── public/
│   │   ├── images/                            # 103 high-resolution food images
│   │   └── favicon.*                          # Icons and branding
│   ├── src/
│   │   ├── components/                        # FoodCard, Navbar, Footer, CategoryNav, etc.
│   │   ├── hooks/                             # useCart custom hook
│   │   ├── pages/                             # Home, Menu, Cart, OrderConfirmation
│   │   ├── lib/                               # API helpers, types, queryClient, utils
│   │   ├── App.tsx                            # Routes & layout
│   │   └── main.tsx                           # React entry point
│   ├── package.json
│   ├── vite.config.ts                         # Proxies /api to Spring Boot (port 8000)
│   └── tsconfig.json
├── .gitignore
├── OOP_IMPLEMENTATION.md                      # Comprehensive Academic OOP Guide
└── README.md                                  # Project Documentation
```

---

## 📜 License
This project is licensed under the MIT License.
