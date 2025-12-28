# 🍊 Zest: Multi-Restaurant Food Ordering System
 
#### **Course:** Java Programming.
#### **Team:**
* **Ahmed Hesham** (Backend Architect & Database) 
* **Ziad Osama** (User Management & Admin Logic) 
* **Hamdy Ashraf** (Customer Experience & Menu Logic)

> 📋 **See [CHANGELOG.md](CHANGELOG.md) for detailed documentation of all changes and improvements made to this project.** 



---

## 📖 Project Overview

Zest is a JavaFX desktop application that simulates a food delivery platform. It demonstrates advanced Object-Oriented Programming principles including **Interfaces**, **Inheritance**, **Exception Handling**, and **Design Patterns** (Singleton, Factory, Adapter, Decorator, Strategy).

### Key Features

* **User Accounts:** Login/Registration for Customers and Merchants.
* **Ordering:** Browse restaurants, search menu items, and add to cart.
* **Pattern Implementation:**
* **Singleton:** Centralized Database Connection.
* **Factory:** Dynamic User creation.
* **Adapter:** Integration with a mock 3rd-party payment system.
* **Decorator:** Dynamic pricing for menu item add-ons (e.g., Extra Cheese).
* **Snapshot:** Preserving order prices at the time of purchase.

---

## 🛠️ Setup & Installation

### 1. Prerequisites

Ensure you have the following installed before writing code:

* **Java JDK 17+** (Required for JavaFX).
* **IntelliJ IDEA** (Community or Ultimate).
* **SceneBuilder** (For editing FXML files).

### 2. Database Setup (Automatic!)

The application uses **SQLite** and automatically creates the database file (`zest_db.sqlite`) in the project root when you first run it. 

**No manual database setup required!** The database will be created with all tables and seed data automatically on first launch.

The database file will be created at: `zest_db.sqlite` (in your project root directory)

### 3. Dependency Check

The project uses SQLite JDBC driver (automatically included in `pom.xml`). No additional database server installation needed!

---

## 📂 strict Project Structure & Roles

To prevent **Merge Conflicts**, do not edit files that belong to another team member.

### 🟥 Ahmed Hesham (The Architect)

* **Responsibility:** Database connectivity, Payment Logic, and Main Entry point.
* **Files You Own:**
* `src/main/java/com/zest/Main.java`
* `src/main/java/com/zest/dao/DBConnection.java` (Singleton Pattern)
* `src/main/java/com/zest/dao/DataService.java` (All SQL Queries)
* `src/main/java/com/zest/logic/PaymentStrategy.java` (Interface)
* `src/main/java/com/zest/logic/PayPalAdapter.java` (Adapter Pattern)
* `src/main/java/com/zest/logic/CashPayment.java`



### 🟦 Ziad Osama (The Gatekeeper)

* **Responsibility:** Authentication, User Profiles, Order History.
* **Files You Own:**
* `src/main/java/com/zest/controller/LoginController.java`
* `src/main/java/com/zest/controller/RegisterController.java`
* `src/main/java/com/zest/controller/HistoryController.java`
* `src/main/java/com/zest/model/User.java` (Parent Class)
* `src/main/java/com/zest/model/Customer.java`
* `src/main/java/com/zest/model/UserFactory.java` (Factory Pattern)
* `src/main/java/com/zest/exceptions/AuthException.java`
* **Views:** `Login.fxml`, `Register.fxml`, `History.fxml`



### 🟩 Hamdy Ashraf (The Chef)

* **Responsibility:** Menu browsing, Search Logic, Cart Management.
* **Files You Own:**
* `src/main/java/com/zest/controller/HomeController.java`
* `src/main/java/com/zest/controller/CartController.java`
* `src/main/java/com/zest/model/MenuItem.java` (Abstract)
* `src/main/java/com/zest/model/Burger.java`
* `src/main/java/com/zest/model/ToppingDecorator.java` (Decorator Pattern)
* `src/main/java/com/zest/model/ExtraCheese.java`
* **Views:** `Home.fxml`, `Cart.fxml`

---

## 🧠 Design Pattern Guide (For Grading)

When asked during the discussion, point to these specific files:

| Pattern | Class / File | Purpose |
| --- | --- | --- |
| **Singleton** | `DBConnection.java` | Ensures only one database connection exists to save resources. |
| **Factory Method** | `UserFactory.java` | Creates `Customer` or `Merchant` objects based on a string input. |
| **Adapter** | `PayPalAdapter.java` | Allows the `PaymentStrategy` interface to use an incompatible 3rd-party API. |
| **Decorator** | `ExtraCheese.java` | Wraps a `MenuItem` to add price/description without modifying the original class. |
| **Strategy** | `PaymentStrategy.java` | Allows swapping payment methods (Cash/Card/PayPal) at runtime.

---

## ⚡ How to Run

1. **Everyone:** Open `Main.java` in IntelliJ.
2. Click the green **Run** button.
3. The database will be automatically created on first run with seed data.
4. **Login Credentials (Dummy Data):**
* *Email:* `ziad@gmail.com`
* *Password:* `123`

---

## 🐛 Troubleshooting

* **"Table doesn't exist"**: The database should auto-create on first run. If this error persists, delete `zest_db.sqlite` and restart the application.
* **"No suitable driver found"**: Reload Maven dependencies in IntelliJ (right-click `pom.xml` → Maven → Reload Project).
* **"FXML file not found"**: Ensure your FXML files are inside `src/main/resources/fxml/` and the path in `Main.java` is correct (`/fxml/Login.fxml`).
* **Database file location**: The SQLite database file (`zest_db.sqlite`) is created in your project root directory. You can delete it to reset the database.