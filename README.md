# 🍊 Zest: Multi-Restaurant Food Ordering System
 
#### **Course:** Java Programming.
#### **Team:**
* **Ahmed Hesham** (Backend Architect & Database) 
* **Ziad Osama** (User Management & Admin Logic) 
* **Hamdy Ashraf** (Customer Experience & Menu Logic)

> 📋 **See [CHANGELOG.md](CHANGELOG.md) for detailed documentation of all changes and improvements made to this project.**
> 📸 **See [IMAGE_GUIDE.md](IMAGE_GUIDE.md) for image requirements and specifications.**

---

## 🚀 Quick Start

**Want to run it right now?**

1. Open `Main.java` in IntelliJ IDEA
2. Click the green ▶️ Run button (or press `Shift + F10`)
3. Login with: `ziad@gmail.com` / `123`
4. Done! 🎉

> 💡 **No database setup needed!** The SQLite database auto-creates on first run.

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

* **Java JDK 17+** (Required for JavaFX). The project is configured for Java 17. If Maven uses a different JDK version, ensure it's Java 17 or later.
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

> ⚠️ **Note**: The project is configured for **Java 17**. Make sure Maven is using Java 17 or later. Check with `mvn -version`. If you have Java 21 installed but Maven uses Java 17, that's fine - the project will compile and run correctly.

### 📝 Test Credentials (Seed Data)

The following test accounts are automatically created:

| Email | Password | Role |
|-------|----------|------|
| `ziad@gmail.com` | `123` | CUSTOMER |
| `hamdy@gmail.com` | `123` | CUSTOMER |
| `ahmed@zest.com` | `123` | MERCHANT |

### ✅ What to Expect

1. **Login Screen** appears first
2. **Database auto-creates** on first run (check console for messages)
3. **Home Screen** shows menu items after login
4. **Cart** allows adding/removing items
5. **Checkout** creates orders in database
6. **History** shows past orders

---

## 🐛 Troubleshooting

* **"Table doesn't exist"**: The database should auto-create on first run. If this error persists, delete `zest_db.sqlite` and restart the application.
* **"No suitable driver found"**: Reload Maven dependencies in IntelliJ (right-click `pom.xml` → Maven → Reload Project).
* **"FXML file not found"**: Ensure your FXML files are inside `src/main/resources/fxml/` and the path in `Main.java` is correct (`/fxml/Login.fxml`).
* **Database file location**: The SQLite database file (`zest_db.sqlite`) is created in your project root directory. You can delete it to reset the database.