# 🍊 Zest: Multi-Restaurant Food Ordering System

#### **Course:** Java Programming.
#### **Team:**
* **Ahmed Hesham** (Backend Architect & Database) 
* **Ziad Osama** (User Management & Order History) 
* **Hamdy Ashraf** (Customer Experience & Menu Logic)

> 📋 **See [CHANGELOG.md](CHANGELOG.md) for detailed documentation of all changes and improvements made to this project.**  
> 📸 **See [IMAGE_GUIDE.md](IMAGE_GUIDE.md) for image requirements and specifications.**  
> ✅ **See [FINAL_STATUS.md](FINAL_STATUS.md) for UML Schema compliance status.**  
> 🔍 **See [VALIDATION_REPORT.md](VALIDATION_REPORT.md) for initial validation findings.**

---

## 🚀 Quick Start

**Want to run it right now?**

1. Open `Main.java` in IntelliJ IDEA
2. Click the green ▶️ Run button (or press `Shift + F10`)
3. Login with: `ziad@gmail.com` / `123`
4. Done! 🎉

> 💡 **No database setup needed!** The SQLite database auto-creates on first run with automatic schema migration.

---

## 📖 Project Overview

Zest is a JavaFX desktop application that simulates a food delivery platform. It demonstrates advanced Object-Oriented Programming principles including **Interfaces**, **Inheritance**, **Exception Handling**, and **Design Patterns** (Singleton, Factory, Adapter, Decorator, Strategy, Snapshot).

### Key Features

* **User Accounts:** Login/Registration for Customers and Merchants with password hashing
* **Restaurant Management:** Multi-restaurant support with merchant ownership
* **Menu Browsing:** Browse restaurants, view menu items with availability status
* **Cart Management:** Add items, adjust quantities, remove items
* **Order Processing:** Complete checkout with payment methods (Cash/PayPal)
* **Order History:** View current and past orders with status tracking
* **Snapshot Pattern:** Preserves order prices at the time of purchase
* **Design Patterns:** Comprehensive implementation of 6+ design patterns

---

## 🛠️ Setup & Installation

### 1. Prerequisites

Ensure you have the following installed before writing code:

* **Java JDK 17+** (Required for JavaFX). The project is configured for Java 17.
* **IntelliJ IDEA** (Community or Ultimate).
* **SceneBuilder** (For editing FXML files).

### 2. Database Setup (Automatic!)

The application uses **SQLite** and automatically creates the database file (`zest_db.sqlite`) in the project root when you first run it. 

**No manual database setup required!** The database will be created with all tables and seed data automatically on first launch. The system includes automatic schema migration for existing databases.

The database file will be created at: `zest_db.sqlite` (in your project root directory)

### 3. Dependency Check

The project uses SQLite JDBC driver (automatically included in `pom.xml`). No additional database server installation needed!

---

## 📂 Project Structure & Team Responsibilities

To prevent **Merge Conflicts**, do not edit files that belong to another team member.

### 🟥 Ahmed Hesham (The Architect)

**Responsibility:** Database connectivity, Payment Logic, Order Processing, Platform Architecture, and Main Entry point.

**Files You Own:**

**Core Infrastructure:**
* `src/main/java/com/zest/Main.java` - Application entry point
* `src/main/java/com/zest/dao/DBConnection.java` - Singleton Pattern (Database Connection)
* `src/main/java/com/zest/dao/DataService.java` - All SQL Queries & Data Access

**Payment System:**
* `src/main/java/com/zest/logic/PaymentStrategy.java` - Strategy Pattern Interface
* `src/main/java/com/zest/logic/PayPalAdapter.java` - Adapter Pattern
* `src/main/java/com/zest/logic/CashPayment.java` - Cash Payment Implementation

**Order Processing:**
* `src/main/java/com/zest/controller/CheckoutController.java` - Checkout & Payment Processing
* `src/main/java/com/zest/model/Order.java` - Order Model (with OrderItems, PaymentStrategy)
* `src/main/java/com/zest/model/OrderItem.java` - **Snapshot Pattern** Implementation
* `src/main/java/com/zest/model/OrderStatus.java` - Order Status Enum

**Platform Architecture:**
* `src/main/java/com/zest/model/Platform.java` - Singleton Pattern (Single Source of Truth)

**Utilities:**
* `src/main/java/com/zest/util/OrderLogger.java` - Order Logging Utility

**Views:**
* `src/main/resources/fxml/Checkout.fxml` - Checkout Screen

---

### 🟦 Ziad Osama (The Gatekeeper)

**Responsibility:** Authentication, User Management, Order History, User Models.

**Files You Own:**

**Authentication & User Management:**
* `src/main/java/com/zest/controller/LoginController.java` - Login Logic
* `src/main/java/com/zest/controller/RegisterController.java` - Registration Logic
* `src/main/java/com/zest/controller/HistoryController.java` - Order History Display

**User Models:**
* `src/main/java/com/zest/model/User.java` - Abstract User Class (with passwordHash & authenticate)
* `src/main/java/com/zest/model/Customer.java` - Customer Model (with orderHistory, placeOrder, leaveReview)
* `src/main/java/com/zest/model/Merchant.java` - Merchant Model (with businessLicense)
* `src/main/java/com/zest/model/UserFactory.java` - Factory Pattern (User Creation)

**Restaurant Management:**
* `src/main/java/com/zest/controller/RestaurantSelectionController.java` - Restaurant Selection
* `src/main/java/com/zest/model/Restaurant.java` - Restaurant Model (with owner, menu, reviews)

**Review System:**
* `src/main/java/com/zest/model/Review.java` - Review Model

**Views:**
* `src/main/resources/fxml/Login.fxml` - Login Screen
* `src/main/resources/fxml/Register.fxml` - Registration Screen
* `src/main/resources/fxml/History.fxml` - Order History Screen
* `src/main/resources/fxml/RestaurantSelection.fxml` - Restaurant Selection Screen

---

### 🟩 Hamdy Ashraf (The Chef)

**Responsibility:** Menu Browsing, Cart Management, Menu Items, Decorator Pattern.

**Files You Own:**

**Menu & Cart:**
* `src/main/java/com/zest/controller/HomeController.java` - Menu Display & Navigation
* `src/main/java/com/zest/controller/CartController.java` - Cart Management
* `src/main/java/com/zest/controller/CartItemController.java` - Cart Item Display
* `src/main/java/com/zest/controller/ItemCardController.java` - Menu Item Card Display
* `src/main/java/com/zest/logic/CartManager.java` - Singleton Pattern (Cart Management)

**Menu Models:**
* `src/main/java/com/zest/model/MenuItem.java` - Menu Item Model (with currentPrice, isAvailable)
* `src/main/java/com/zest/model/ToppingDecorator.java` - Decorator Pattern (Abstract)
* `src/main/java/com/zest/model/ExtraCheese.java` - Decorator Pattern (Concrete Implementation)

**Group Orders:**
* `src/main/java/com/zest/model/GroupOrder.java` - Group Order Model (extends Order, bill splitting)

**Views:**
* `src/main/resources/fxml/Home.fxml` - Menu Screen
* `src/main/resources/fxml/Cart.fxml` - Cart Screen
* `src/main/resources/fxml/CartItemCard.fxml` - Cart Item Card
* `src/main/resources/fxml/ItemCard.fxml` - Menu Item Card

---

## 🏗️ Architecture Overview

### Class Hierarchy

```
User (Abstract)
├── Customer
│   ├── orderHistory: List<Order>
│   ├── placeOrder(Order)
│   └── leaveReview(Review)
└── Merchant
    ├── businessLicense: String
    ├── addMenuItem(MenuItem)
    ├── updatePrice(MenuItem, double)
    └── purgeData()

MenuItem
├── Burger (removed - unused)
└── ToppingDecorator (Abstract)
    └── ExtraCheese

Order
├── items: List<OrderItem> (Snapshot Pattern)
├── paymentMethod: PaymentStrategy
├── status: OrderStatus
└── GroupOrder (extends Order)
    ├── participantSplits: Map<Customer, List<OrderItem>>
    └── splitBill()
```

### Database Schema

**Tables:**
- `users` - User accounts (with password_hash, business_license)
- `restaurants` - Restaurant information (with merchant_id)
- `menu_items` - Menu items (with current_price, is_available)
- `orders` - Order records (with total_amount, status enum)
- `order_items` - Order items with **snapshot prices** (price_at_purchase)
- `reviews` - Customer reviews

---

## 🧠 Design Pattern Guide (For Grading)

When asked during the discussion, point to these specific files:

| Pattern | Class / File | Purpose | Implemented By |
| --- | --- | --- | --- |
| **Singleton** | `DBConnection.java` | Ensures only one database connection exists to save resources. | Ahmed |
| **Singleton** | `CartManager.java` | Single cart instance across the application. | Hamdy |
| **Singleton** | `Platform.java` | Single source of truth for restaurants and users. | Ahmed |
| **Factory Method** | `UserFactory.java` | Creates `Customer` or `Merchant` objects based on role string. | Ziad |
| **Adapter** | `PayPalAdapter.java` | Allows the `PaymentStrategy` interface to use an incompatible 3rd-party API. | Ahmed |
| **Decorator** | `ExtraCheese.java` | Wraps a `MenuItem` to add price/description without modifying the original class. | Hamdy |
| **Strategy** | `PaymentStrategy.java` | Allows swapping payment methods (Cash/PayPal) at runtime. | Ahmed |
| **Snapshot** | `OrderItem.java` | Preserves menu item prices at the time of purchase (priceAtPurchase). | Ahmed |

---

## ⚡ How to Run

### Method 1: Using IntelliJ IDEA (Recommended)

1. **Open the Project:**
   - Open IntelliJ IDEA
   - File → Open → Select the `zest` project folder
   - Wait for IntelliJ to index the project and download Maven dependencies

2. **Load Maven Dependencies:**
   - Right-click on `pom.xml` → Maven → Reload Project
   - Wait for dependencies to download (you'll see progress in the bottom status bar)

3. **Run the Application:**
   - Navigate to `src/main/java/com/zest/Main.java`
   - Right-click on `Main.java` → Run 'Main.main()'
   - OR click the green ▶️ Run button next to the `main()` method
   - OR press `Shift + F10`

4. **First Run:**
   - The application will automatically create `zest_db.sqlite` in your project root
   - Database tables and seed data will be initialized automatically
   - You'll see console messages: "Initializing database schema and seed data..."
   - If database exists, you'll see: "Database already exists. Migrating schema..."

5. **Login:**
   - Use the test credentials:
     - **Email:** `ziad@gmail.com`
     - **Password:** `123`
   - Or register a new account

### Method 2: Using Maven Command Line

1. **Open Terminal/Command Prompt** in the project root directory

2. **Compile the project:**
   ```bash
   mvn clean compile
   ```

3. **Run the application:**
   ```bash
   mvn javafx:run
   ```

> ⚠️ **Note**: The project is configured for **Java 17**. Make sure Maven is using Java 17 or later. Check with `mvn -version`.

### 📝 Test Credentials (Seed Data)

The following test accounts are automatically created:

| Email | Password | Role | Business License |
|-------|----------|------|-----------------|
| `ziad@gmail.com` | `123` | CUSTOMER | - |
| `hamdy@gmail.com` | `123` | CUSTOMER | - |
| `ahmed@zest.com` | `123` | MERCHANT | BL-2024-001 |

### ✅ What to Expect

1. **Login Screen** appears first
2. **Database auto-creates** on first run (check console for messages)
3. **Restaurant Selection** - Choose a restaurant
4. **Menu Screen** shows menu items after restaurant selection
5. **Cart** allows adding/removing items with quantity management
6. **Checkout** creates orders with snapshot prices and payment processing
7. **History** shows current and past orders with status indicators

---

## 🔑 Key Features Explained

### Snapshot Pattern (Critical Feature)

The **OrderItem** class implements the Snapshot Pattern to preserve menu item prices at the time of purchase. This ensures that:

- Historical orders show correct prices even if menu prices change later
- `priceAtPurchase` is set during OrderItem construction
- Calculations use `priceAtPurchase`, NOT the live `currentPrice` from MenuItem

**Location:** `src/main/java/com/zest/model/OrderItem.java`

### Password Security

- User passwords are hashed using SHA-256
- `User.authenticate()` method validates passwords securely
- Database stores both `password` (for backward compatibility) and `password_hash`

**Location:** `src/main/java/com/zest/model/User.java`

### Order Status Management

- Uses `OrderStatus` enum (PENDING, PREPARING, READY, DELIVERED)
- Type-safe status handling
- Automatic conversion from String for backward compatibility

**Location:** `src/main/java/com/zest/model/OrderStatus.java`

---

## 🐛 Troubleshooting

* **"Table doesn't exist"**: The database should auto-create on first run. If this error persists, delete `zest_db.sqlite` and restart the application.
* **"No suitable driver found"**: Reload Maven dependencies in IntelliJ (right-click `pom.xml` → Maven → Reload Project).
* **"FXML file not found"**: Ensure your FXML files are inside `src/main/resources/fxml/` and the path in `Main.java` is correct (`/fxml/Login.fxml`).
* **Database file location**: The SQLite database file (`zest_db.sqlite`) is created in your project root directory. You can delete it to reset the database.
* **Schema migration**: If you see "Database already exists. Migrating schema..." - this is normal. The system automatically adds new columns to existing databases.

---

## 📊 Project Statistics

* **Total Classes:** 20+
* **Design Patterns:** 8 (Singleton x3, Factory, Adapter, Decorator, Strategy, Snapshot)
* **Controllers:** 8
* **FXML Views:** 9
* **Database Tables:** 6
* **UML Schema Compliance:** 100%

---

## 📚 Additional Documentation

* **[CHANGELOG.md](CHANGELOG.md)** - Detailed change history
* **[VALIDATION_REPORT.md](VALIDATION_REPORT.md)** - Initial validation findings
* **[IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md)** - UML Schema implementation details
* **[DATABASE_AND_CLEANUP_SUMMARY.md](DATABASE_AND_CLEANUP_SUMMARY.md)** - Database updates and cleanup
* **[FXML_CONTROLLER_VALIDATION.md](FXML_CONTROLLER_VALIDATION.md)** - FXML-Controller synchronization
* **[FINAL_STATUS.md](FINAL_STATUS.md)** - Final project status

---

## 🎓 Learning Objectives Achieved

✅ **Object-Oriented Programming:**
- Abstract classes (User)
- Inheritance (Customer, Merchant extend User)
- Polymorphism (PaymentStrategy implementations)
- Encapsulation (private fields with getters/setters)

✅ **Design Patterns:**
- Singleton (DBConnection, CartManager, Platform)
- Factory (UserFactory)
- Adapter (PayPalAdapter)
- Decorator (ExtraCheese)
- Strategy (PaymentStrategy)
- Snapshot (OrderItem)

✅ **Database Management:**
- SQLite integration
- Automatic schema creation
- Schema migration
- Foreign key relationships

✅ **JavaFX:**
- FXML-based UI
- Controller pattern
- Scene navigation
- Event handling

---

**Last Updated:** After major UML Schema refactoring  
**Status:** ✅ Production Ready - 100% UML Compliant
