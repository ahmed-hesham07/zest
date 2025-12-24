# Zest - Multi-Restaurant Food Ordering System (Phase 2)

**Team:** Ahmed Hesham, Ziad Osama, Hamdy Ashraf
**Deadline:** Sunday, Dec 28, 2025
**Goal:** JavaFX Application with Database, Design Patterns, and Exception Handling.

---

## 🚨 THE GOLDEN RULES (Read First)

1. **Do NOT touch another person's files.** Check the "Roles" section below to see which files belong to you.
2. **Do NOT change the folder structure.** It is set up to prevent merge conflicts.
3. **Ahmed** is the only one who can change the Database Schema or `model` classes.

---

## 🛠️ Setup & Installation

### 1. Prerequisites

* **IntelliJ IDEA** (Community or Ultimate)
* **Java JDK 21** (or 17+)
* **MySQL Workbench** (or SQLite)
* **JavaFX SceneBuilder**

### 2. Database Setup (Task for Ahmed)

Run this SQL script in your Workbench to create the necessary tables.

```sql
CREATE DATABASE zest_db;
USE zest_db;

-- 1. Users Table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    email VARCHAR(50) UNIQUE,
    password VARCHAR(50),
    role VARCHAR(20) -- 'CUSTOMER'
);

-- 2. Orders Table
CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    total_price DOUBLE,
    status VARCHAR(20) DEFAULT 'PENDING',
    date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 3. Restaurants & Menu (Simplified for Phase 2)
-- We will hardcode menu items in Java to make the Decorator Pattern easier.

```

---

## 📂 Project Structure & Ownership

Each file has an **Owner**. Only the owner edits that file.

```text
src/main/java/com/zest/
├── Main.java                        [AHMED]
├── model/                           (Data Classes)
│   ├── User.java                    [HAMDY - Parent Class]
│   ├── Customer.java                [HAMDY - Child Class]
│   ├── UserFactory.java             [HAMDY - Factory Pattern]
│   ├── MenuItem.java                [ZIAD - Abstract Class]
│   ├── Burger.java                  [ZIAD]
│   ├── ToppingDecorator.java        [ZIAD - Decorator Pattern]
│   ├── ExtraCheese.java             [ZIAD]
│   └── Order.java                   [AHMED]
├── dao/                             (Database Code)
│   ├── DBConnection.java            [AHMED - Singleton Pattern]
│   └── DataService.java             [AHMED - All SQL Queries]
├── logic/                           (Business Logic)
│   ├── PaymentStrategy.java         [AHMED - Interface]
│   ├── PayPalAdapter.java           [AHMED - Adapter Pattern]
│   └── CashPayment.java             [AHMED]
├── controller/                      (The Screens)
│   ├── LoginController.java         [HAMDY]
│   ├── RegisterController.java      [HAMDY]
│   ├── HomeController.java          [ZIAD - Has Overloading]
│   ├── CartController.java          [ZIAD]
│   └── HistoryController.java       [HAMDY]
└── exceptions/
    └── AuthException.java           [HAMDY]

```

---

## 👨‍💻 Roles & Tasks

### 1. Ahmed Hesham (The Architect)

**Focus:** Backend, Database, Payment.

* **Singleton Pattern:** Implement `DBConnection.java` so only one connection exists.
* **Adapter Pattern:** Implement `PayPalAdapter.java` to wrap a fake 3rd party payment system.
* **Database:** Implement `DataService.java` to handle `login()` and `saveOrder()`.
* **Main Entry:** Set up `Main.java` to launch the Login Screen.

### 2. Hamdy Ashraf (The Gatekeeper)

**Focus:** User Management, Login, History.

* **Factory Pattern:** Implement `UserFactory.java` to create Users.
* *Code:* `return new Customer(name, email);`


* **Exception Handling:** Create `AuthException.java`. Throw this in Login if the password is wrong.
* **UI Tasks:** Build `Login.fxml`, `Register.fxml`, and `History.fxml` (Use TableView).

### 3. Ziad Osama (The Chef)

**Focus:** Menu, Cart, Ordering.

* **Decorator Pattern:** Implement `ToppingDecorator` and `ExtraCheese`.
* *Logic:* Wrapping a Burger object (`new ExtraCheese(new Burger())`) increases the price.


* **Overloading:** In `HomeController.java`, create two search methods:
* `search(String name)`
* `search(double rating)`


* **UI Tasks:** Build `Home.fxml` (Restaurant List) and `Cart.fxml`.

---

## 🏗️ Design Pattern Implementation Guide (For the Report)

When writing the report, refer to these specific files:

| Pattern | File Location | Description |
| --- | --- | --- |
| **Singleton** | `com.zest.dao.DBConnection` | Ensures 1 DB connection instance. |
| **Factory Method** | `com.zest.model.UserFactory` | Centralizes object creation. |
| **Decorator** | `com.zest.model.ExtraCheese` | Adds functionality (price) dynamically. |
| **Adapter** | `com.zest.logic.PayPalAdapter` | Makes incompatible interfaces work together. |

---

## 🚀 How to Run the Project

1. **Ahmed:** Ensure MySQL is running.
2. **Everyone:** Open `src/main/java/com/zest/Main.java`.
3. Click the **Green Play Button**.
4. The Login Screen should appear.

---

## ⚠️ Troubleshooting

* **"Class not found":** Right-click `src` -> Reload from Disk. Then `File` -> `Invalidate Caches`.
* **"SQL Exception":** Check `DBConnection.java`. Is the password for root correct? (`root` / `password` or empty).
* **"FXML Error":** Make sure your Controller name in SceneBuilder matches exactly (`com.zest.controller.LoginController`).