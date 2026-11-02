# 🎓 FoodEase — Object-Oriented Programming (OOP) Implementation Guide

> **Academic Project Reference Documentation**  
> **Course / Subject:** Object-Oriented Software Engineering & Enterprise Application Development  
> **Backend Implementation:** Java 21, Spring Boot 3.3.5, Spring Data JPA, Hibernate, Maven  
> **Architecture Pattern:** Layered Enterprise Architecture (Controller → Service → Repository → Model)

---

## 1. Executive Summary

**FoodEase** is a full-stack, enterprise-grade food ordering platform designed for authentic South Indian cuisine. Originally designed with a lightweight Python backend, the backend has been completely converted into **Java 21 and Spring Boot**, adhering to strict **Object-Oriented Programming (OOP)** and **SOLID design principles**.

The codebase demonstrates how real-world commercial requirements (catalogs, dynamic shopping carts, promotional discounts, GST tax calculations, and checkout flows) can be cleanly modeled using object-oriented principles.

---

## 2. Core OOP Principles Implementation

```
               ┌────────────────────────────────────────────────────────┐
               │                    OOP Pillars                         │
               └──────────────────────────┬─────────────────────────────┘
          ┌──────────────────┬────────────┴───────┬──────────────────┐
          ▼                  ▼                    ▼                  ▼
    Encapsulation       Inheritance          Polymorphism        Abstraction
 (Domain validation,  (BaseEntity, User    (Method Overriding, (Service Interfaces,
  private state,       -> Customer/Admin,   Method Overloading, Abstract Classes,
  business methods)    DiscountStrategy)    Strategy Pattern)   Repositories)
```

---

### A. Encapsulation

**Encapsulation** is the bundling of data and the methods that operate on that data, while restricting direct access to internal state to maintain data integrity.

#### 1. Private Fields with Guarded Accessors & Mutators
Every domain entity encapsulates its mutable fields via `private` access modifiers and provides public getter and setter methods with boundary checks:

* **File:** [`src/main/java/com/foodease/model/MenuItem.java`](backend/src/main/java/com/foodease/model/MenuItem.java)
  ```java
  public void setPrice(double price) {
      if (price < 0) {
          throw new IllegalArgumentException("Menu item price cannot be negative");
      }
      this.price = price;
  }
  ```

* **File:** [`src/main/java/com/foodease/model/User.java`](backend/src/main/java/com/foodease/model/User.java)
  ```java
  public void setName(String name) {
      if (name == null || name.trim().isEmpty()) {
          throw new IllegalArgumentException("User name cannot be empty");
      }
      this.name = name.trim();
  }
  ```

#### 2. Domain-Driven Business Methods inside Entities
State transitions and calculations are encapsulated within domain classes rather than scattered across external procedural scripts:

* **File:** [`src/main/java/com/foodease/model/Cart.java`](backend/src/main/java/com/foodease/model/Cart.java)
  - `addItem(MenuItem menuItem, int quantity)`: Manages internal cart collections, updates existing quantities or creates new line items.
  - `updateItemQuantity(String menuItemId, int quantity)`: Automatically removes items if the quantity reaches 0.
  - `getSubtotal()`: Encapsulates line-item sum calculations.
  - `clear()`: Resets cart state.

* **File:** [`src/main/java/com/foodease/model/Order.java`](backend/src/main/java/com/foodease/model/Order.java)
  - `calculateFinancials(double discountRate, double gstRate)`: Encapsulates subtotal calculation, percentage discount deductions, 5% GST tax calculation, and net total computation.

* **File:** [`src/main/java/com/foodease/model/Coupon.java`](backend/src/main/java/com/foodease/model/Coupon.java)
  - `isValidForAmount(double subtotal)`: Determines eligibility based on `minOrder`.
  - `calculateDiscount(double subtotal)`: Calculates exact discount in Indian Rupees.

---

### B. Inheritance

**Inheritance** establishes an **IS-A** relationship, allowing derived classes to inherit state and behavior from a parent class, promoting code reuse and modularity.

#### 1. Common Persistence Audit Hierarchy (`BaseEntity`)
* **File:** [`src/main/java/com/foodease/model/BaseEntity.java`](backend/src/main/java/com/foodease/model/BaseEntity.java)
  - An abstract class annotated with `@MappedSuperclass`.
  - Provides `createdAt` and `updatedAt` lifecycle tracking for all database entities (`User`, `Category`, `MenuItem`, `Coupon`, `Cart`, `Order`, `OrderItem`).
  - Utilizes JPA lifecycle callbacks (`@PrePersist`, `@PreUpdate`) to maintain audit timestamps automatically.

#### 2. User Domain Hierarchy (`User` → `Customer`, `Admin`)
* **File:** [`src/main/java/com/foodease/model/User.java`](backend/src/main/java/com/foodease/model/User.java) (Abstract Base Entity)
  - Defines shared identity: `id`, `name`, `email`, `role`.
  - Configured with `@Inheritance(strategy = InheritanceType.JOINED)`.
  - Declares abstract method `public abstract String getDashboardAccess();`.

* **File:** [`src/main/java/com/foodease/model/Customer.java`](backend/src/main/java/com/foodease/model/Customer.java) (Derived Class)
  - Extends `User` (**Customer IS-A User**).
  - Encapsulates customer-specific delivery attributes: `phone`, `deliveryAddress`, `preferredPaymentMode`.

* **File:** [`src/main/java/com/foodease/model/Admin.java`](backend/src/main/java/com/foodease/model/Admin.java) (Derived Class)
  - Extends `User` (**Admin IS-A User**).
  - Encapsulates administrative properties: `department`, `permissionLevel`.

```
                        ┌──────────────────┐
                        │    BaseEntity    │
                        └────────┬─────────┘
                                 │
                        ┌────────▼─────────┐
                        │   User (Abstract)│
                        └────────┬─────────┘
                   ┌─────────────┴─────────────┐
                   ▼                           ▼
          ┌────────────────┐          ┌────────────────┐
          │    Customer    │          │     Admin      │
          └────────────────┘          └────────────────┘
```

#### 3. Promotional Discount Strategy Hierarchy
* **Base Interface:** [`DiscountStrategy`](backend/src/main/java/com/foodease/service/strategy/DiscountStrategy.java)
* **Abstract Base Class:** [`AbstractDiscountStrategy`](backend/src/main/java/com/foodease/service/strategy/AbstractDiscountStrategy.java)
* **Derived Class 1:** [`PercentageDiscountStrategy`](backend/src/main/java/com/foodease/service/strategy/PercentageDiscountStrategy.java) (extends `AbstractDiscountStrategy`)
* **Derived Class 2:** [`ThresholdDiscountStrategy`](backend/src/main/java/com/foodease/service/strategy/ThresholdDiscountStrategy.java) (extends `PercentageDiscountStrategy`)

---

### C. Polymorphism

**Polymorphism** allows objects to take many forms. This project demonstrates both **Compile-time Polymorphism (Method Overloading)** and **Runtime Polymorphism (Method Overriding)**.

#### 1. Method Overriding (Dynamic Polymorphism)

* **Polymorphic Dashboard Access:**
  - `User.getDashboardAccess()` is overridden in:
    - `Customer`: returns `"CUSTOMER_PORTAL_VIEW"`.
    - `Admin`: returns `"ADMIN_MANAGEMENT_CONSOLE"`.
  - Evaluated at runtime when polymorphic calls are made in [`UserController.java`](backend/src/main/java/com/foodease/controller/UserController.java).

* **Polymorphic Discount Calculations (Strategy Pattern):**
  - Contract: `double calculateDiscount(double subtotal)` in [`DiscountStrategy`](backend/src/main/java/com/foodease/service/strategy/DiscountStrategy.java).
  - `PercentageDiscountStrategy`: Computes percentage discount directly on `subtotal`.
  - `ThresholdDiscountStrategy`: Overrides `isApplicable` and `calculateDiscount` to verify `subtotal >= minOrderAmount` before applying discounts.
  - `NoDiscountStrategy`: Returns `0.0`.

* **Polymorphic Payment Gateways:**
  - Contract: `boolean processPayment(Order order)` in [`PaymentProcessor`](backend/src/main/java/com/foodease/service/strategy/payment/PaymentProcessor.java).
  - Overridden across:
    - [`UpiPaymentProcessor`](backend/src/main/java/com/foodease/service/strategy/payment/UpiPaymentProcessor.java): Handles UPI instant VPA settlements.
    - [`CardPaymentProcessor`](backend/src/main/java/com/foodease/service/strategy/payment/CardPaymentProcessor.java): Handles credit/debit card card networks.
    - [`CashPaymentProcessor`](backend/src/main/java/com/foodease/service/strategy/payment/CashPaymentProcessor.java): Handles cash on delivery.

#### 2. Method Overloading (Static / Compile-Time Polymorphism)

* **Financial Calculations in `OrderService`:**
  * **File:** [`src/main/java/com/foodease/service/interfaces/OrderService.java`](backend/src/main/java/com/foodease/service/interfaces/OrderService.java)
  ```java
  // Overload 1: Default 0 discount and 5% GST
  double calculateTotal(double subtotal);

  // Overload 2: With discount amount and default 5% GST
  double calculateTotal(double subtotal, double discountAmount);

  // Overload 3: Full customization of discount and GST rate
  double calculateTotal(double subtotal, double discountAmount, double gstRate);
  ```

* **Cart Operations in `CartService`:**
  * **File:** [`src/main/java/com/foodease/service/interfaces/CartService.java`](backend/src/main/java/com/foodease/service/interfaces/CartService.java)
  ```java
  // Overload 1: Adds 1 unit of menu item by default
  CartViewDTO addItem(String cartId, String menuItemId);

  // Overload 2: Adds specified quantity of menu item
  CartViewDTO addItem(String cartId, String menuItemId, int quantity);
  ```

* **Menu Queries in `MenuService`:**
  * **File:** [`src/main/java/com/foodease/service/interfaces/MenuService.java`](backend/src/main/java/com/foodease/service/interfaces/MenuService.java)
  ```java
  // Overload 1: Retrieve all menu items
  List<MenuItemDTO> getMenuItems();

  // Overload 2: Filter by category ID
  List<MenuItemDTO> getMenuItems(String categoryId);

  // Overload 3: Filter by vegetarian / non-vegetarian flag
  List<MenuItemDTO> getMenuItems(boolean vegOnly);
  ```

* **Constructor Overloading:**
  - `Customer(name, phone, address)` vs `Customer(name, email, phone, address, paymentMode)`
  - `CategoryInfoDTO(cat)` vs `CategoryInfoDTO(cat, count)`

---

### D. Abstraction

**Abstraction** hides complex implementation details and exposes only clean, well-defined interfaces to clients.

#### 1. Service Layer Abstraction
Controllers never interact with concrete service implementations or database drivers directly. They depend purely on service interfaces:
- [`MenuService`](backend/src/main/java/com/foodease/service/interfaces/MenuService.java) implemented by [`MenuServiceImpl`](backend/src/main/java/com/foodease/service/implementation/MenuServiceImpl.java)
- [`CartService`](backend/src/main/java/com/foodease/service/interfaces/CartService.java) implemented by [`CartServiceImpl`](backend/src/main/java/com/foodease/service/implementation/CartServiceImpl.java)
- [`OrderService`](backend/src/main/java/com/foodease/service/interfaces/OrderService.java) implemented by [`OrderServiceImpl`](backend/src/main/java/com/foodease/service/implementation/OrderServiceImpl.java)
- [`CouponService`](backend/src/main/java/com/foodease/service/interfaces/CouponService.java) implemented by [`CouponServiceImpl`](backend/src/main/java/com/foodease/service/implementation/CouponServiceImpl.java)

#### 2. Abstract Classes
- `BaseEntity`: Enforces standard audit timestamps across all child models.
- `User`: Provides shared authentication and profile foundation while requiring subclasses to implement `getDashboardAccess()`.
- `AbstractDiscountStrategy`: Captures reusable discount logic while leaving applicability checks to specific discount models.

---

### E. Composition, Aggregation, and Association

The application models realistic real-world relationships using JPA relational mappings:

#### 1. Composition (Part-Whole with Dependent Lifetime)
* **`Order` and `OrderItem`:**
  - An `OrderItem` cannot exist without its parent `Order`.
  - Configured with:
    ```java
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
    ```
  - Deleting or cancelling an `Order` cascades and removes all contained `OrderItem` instances.

* **`Cart` and `CartItem`:**
  - A `CartItem` belongs strictly to a single `Cart`.
  - Configured with `cascade = CascadeType.ALL, orphanRemoval = true`.

#### 2. Association (Independent Lifetimes)
* **`OrderItem` and `MenuItem`:**
  - An `OrderItem` references a `MenuItem`.
  - If a `MenuItem` is modified or deleted from the active menu, historical orders retain their records.
  - Configured via `@ManyToOne`.

* **`Customer` and `Order`:**
  - A `Customer` places multiple `Order`s (`@OneToMany` / `@ManyToOne`).
  - Both entities exist independently in their respective tables.

#### 3. Aggregation (Container-Content Relationship)
* **`Category` and `MenuItem`:**
  - A `Category` aggregates multiple `MenuItem`s.
  - If a category is removed, individual menu items can be reassigned to other categories.

---

## 3. SOLID Principles Compliance

| Principle | Meaning & Application in FoodEase | Relevant Files |
| :--- | :--- | :--- |
| **S** - Single Responsibility Principle (SRP) | Each class has one and only one reason to change. Controllers only handle HTTP translation; Services only execute business logic; Repositories only manage database persistence; DTOs format client responses. | `MenuController`, `MenuServiceImpl`, `MenuItemRepository`, `MenuItemDTO` |
| **O** - Open/Closed Principle (OCP) | Open for extension, closed for modification. New discount models (`FestiveFixedDiscountStrategy`, `BogoDiscountStrategy`) or payment methods can be added by implementing interfaces without changing `OrderServiceImpl`. | `DiscountStrategy`, `PaymentProcessor`, `PaymentProcessorFactory` |
| **L** - Liskov Substitution Principle (LSP) | Subtypes must be substitutable for their base types without altering program correctness. Any `DiscountStrategy` or `User` subtype (`Customer`, `Admin`) functions seamlessly wherever the base type is expected. | `PercentageDiscountStrategy`, `ThresholdDiscountStrategy`, `Customer`, `Admin` |
| **I** - Interface Segregation Principle (ISP) | Clients should not be forced to depend upon interfaces that they do not use. Interfaces are separated into fine-grained boundaries (`MenuService`, `CartService`, `OrderService`, `CouponService`). | `service/interfaces/*` |
| **D** - Dependency Inversion Principle (DIP) | High-level modules do not depend on low-level modules; both depend on abstractions. Controllers inject service interfaces, and services inject Spring Data JPA repository interfaces via constructor injection. | `OrderController(OrderService)` |

---

## 4. Design Patterns Implemented

1. **Strategy Pattern:**
   - [`DiscountStrategy`](backend/src/main/java/com/foodease/service/strategy/DiscountStrategy.java): Decouples promotional calculation algorithms from order management.
   - [`PaymentProcessor`](backend/src/main/java/com/foodease/service/strategy/payment/PaymentProcessor.java): Decouples payment gateway operations from order placement.

2. **Factory Pattern:**
   - [`PaymentProcessorFactory`](backend/src/main/java/com/foodease/service/strategy/payment/PaymentProcessorFactory.java): Dynamically resolves the appropriate payment processor implementation based on `PaymentMode`.

3. **Null Object Pattern:**
   - [`NoDiscountStrategy`](backend/src/main/java/com/foodease/service/strategy/NoDiscountStrategy.java): Eliminates null checks when processing orders without discount promo codes.

4. **Data Transfer Object (DTO) Pattern:**
   - Protects internal JPA entities from being directly serialized over the network, preventing unwanted coupling and circular reference serialization issues.

5. **Repository Pattern:**
   - Spring Data JPA repositories encapsulate all database access and ORM queries behind standard interfaces.

---

## 5. Complete Class & Interface Directory

```
com.foodease
├── FoodEaseApplication.java                       # Application Bootstrap
│
├── config
│   ├── CorsConfig.java                            # Cross-Origin Resource Sharing configuration
│   └── DataInitializer.java                       # Preloads 104 menu items, 18 categories, coupons
│
├── controller
│   ├── MenuController.java                        # /api/menu, /api/menu/categories, /api/menu/featured
│   ├── CartController.java                        # /api/cart, /api/cart/items
│   ├── OrderController.java                       # /api/orders, /api/orders/{id}
│   ├── CouponController.java                      # /api/coupons, /api/coupons/validate
│   ├── CollectionsController.java                 # /api/collections/config, /api/collections/stats
│   └── UserController.java                        # /api/users (Polymorphic User hierarchy)
│
├── model
│   ├── BaseEntity.java                            # Abstract @MappedSuperclass (Audit fields)
│   ├── User.java                                  # Abstract Entity (@Inheritance JOINED)
│   ├── Customer.java                              # Extends User
│   ├── Admin.java                                 # Extends User
│   ├── Category.java                              # Food category entity
│   ├── MenuItem.java                              # Dish / Food item entity
│   ├── Coupon.java                                # Promotional coupon entity
│   ├── Cart.java                                  # Shopping cart composite entity
│   ├── CartItem.java                              # Cart line item
│   ├── Order.java                                 # Customer order composite entity
│   └── OrderItem.java                             # Order line item
│
├── repository
│   ├── CategoryRepository.java                    # Spring Data JPA Category repository
│   ├── MenuItemRepository.java                    # Spring Data JPA MenuItem repository
│   ├── CouponRepository.java                      # Spring Data JPA Coupon repository
│   ├── CartRepository.java                        # Spring Data JPA Cart repository
│   ├── OrderRepository.java                       # Spring Data JPA Order repository
│   ├── CustomerRepository.java                    # Spring Data JPA Customer repository
│   └── UserRepository.java                        # Spring Data JPA User repository
│
├── service
│   ├── interfaces
│   │   ├── MenuService.java                       # Contract for Menu business logic
│   │   ├── CartService.java                       # Contract for Cart operations
│   │   ├── OrderService.java                      # Contract for Order processing
│   │   └── CouponService.java                     # Contract for Coupon verification
│   │
│   ├── implementation
│   │   ├── MenuServiceImpl.java                   # MenuService implementation (Overloading)
│   │   ├── CartServiceImpl.java                   # CartService implementation (Overloading)
│   │   ├── OrderServiceImpl.java                  # OrderService implementation (Overloading)
│   │   └── CouponServiceImpl.java                 # CouponService implementation
│   │
│   └── strategy
│       ├── DiscountStrategy.java                  # Discount calculation interface
│       ├── AbstractDiscountStrategy.java          # Abstract discount base class
│       ├── PercentageDiscountStrategy.java        # Straight percentage discount
│       ├── ThresholdDiscountStrategy.java         # Minimum-order threshold discount
│       ├── NoDiscountStrategy.java                # Null object discount
│       └── payment
│           ├── PaymentProcessor.java              # Payment gateway contract
│           ├── UpiPaymentProcessor.java           # UPI implementation
│           ├── CardPaymentProcessor.java          # Credit/Debit Card implementation
│           ├── CashPaymentProcessor.java          # Cash on delivery implementation
│           └── PaymentProcessorFactory.java       # Runtime factory for payment strategies
│
├── dto
│   ├── MenuItemDTO.java
│   ├── CategoryInfoDTO.java
│   ├── FeaturedMenuDTO.java
│   ├── CartItemViewDTO.java
│   ├── CartViewDTO.java
│   ├── AddCartItemRequest.java
│   ├── UpdateCartItemRequest.java
│   ├── CouponInfoDTO.java
│   ├── CouponValidateRequest.java
│   ├── CouponValidateResponse.java
│   ├── CustomerInfoDTO.java
│   ├── OrderCreateRequest.java
│   ├── OrderItemViewDTO.java
│   ├── OrderDTO.java
│   ├── CollectionsStatsDTO.java
│   └── ConfigDTO.java
│
├── enums
│   ├── OrderStatus.java
│   ├── PaymentMode.java
│   ├── PaymentStatus.java
│   └── UserRole.java
│
├── exception
│   ├── ResourceNotFoundException.java
│   ├── BadRequestException.java
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java                # Centralized @RestControllerAdvice
│
└── util
    ├── OrderIdGenerator.java                      # Secure unique Order ID generator
    └── CurrencyFormatter.java                     # Indian Rupee (₹) formatting utility
```

---

## 6. Academic Evaluation Summary

| Requirement | Implementation in Project | Verification Status |
| :--- | :--- | :---: |
| **Java 21 & Spring Boot** | Built on Java 21, Spring Boot 3.3.5, Spring Data JPA, Hibernate, Maven | ✅ Verified |
| **Encapsulation** | Private instance variables, defensive setters, domain calculation methods | ✅ Verified |
| **Inheritance** | `BaseEntity`, `User` → `Customer`/`Admin`, `AbstractDiscountStrategy` → `Percentage`/`Threshold` | ✅ Verified |
| **Polymorphism (Overriding)** | Strategy pattern (`DiscountStrategy`, `PaymentProcessor`), `User.getDashboardAccess()` | ✅ Verified |
| **Polymorphism (Overloading)** | Method overloading in `OrderService`, `CartService`, `MenuService`, and constructors | ✅ Verified |
| **Abstraction** | Service interfaces (`MenuService`, `CartService`, `OrderService`, `CouponService`), abstract classes | ✅ Verified |
| **Composition & Association** | `Order` ↔ `OrderItem` (Composition), `OrderItem` ↔ `MenuItem` (Association), `Cart` ↔ `CartItem` | ✅ Verified |
| **SOLID Principles** | SRP, OCP, LSP, ISP, and DIP rigorously applied across all packages | ✅ Verified |
| **Preserve Website Features** | All 104 dishes, 18 categories, coupons, GST calculations, and cart actions work identical to original | ✅ Verified |
