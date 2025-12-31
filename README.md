<p align="center">
  <img src="https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java"/>
  <img src="https://img.shields.io/badge/JavaFX-17.0.2-0095D5?style=for-the-badge&logo=java&logoColor=white" alt="JavaFX"/>
  <img src="https://img.shields.io/badge/SQLite-3.0-003B57?style=for-the-badge&logo=sqlite&logoColor=white" alt="SQLite"/>
  <img src="https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven"/>
</p>

<h1 align="center">Zest</h1>
<h3 align="center">Multi-Restaurant Food Ordering System</h3>

<p align="center">
  A modern JavaFX desktop application simulating a complete food delivery platform with multi-role authentication, real-time order management, and comprehensive restaurant operations.
</p>

---

## ✨ Features

### For Customers
- **Browse Restaurants** — Explore multiple restaurants with detailed menus
- **Smart Cart Management** — Add items, adjust quantities, and manage orders seamlessly
- **Secure Checkout** — Multiple payment options (Cash/PayPal) with order confirmation
- **Order Tracking** — Real-time status updates from placement to delivery
- **Order History** — View past orders with complete details

### For Merchants
- **Dashboard Overview** — Comprehensive view of restaurant operations and statistics
- **Order Management** — Track and update order status (Pending → Preparing → Ready → Delivered)
- **Menu Control** — Add, edit, and manage menu items with pricing and availability
- **Photo Upload** — Visual menu customization with image support

---

## 🚀 Quick Start

```bash
# Clone the repository
git clone https://github.com/ahmed-hesham07/zest.git

# Navigate to project directory
cd zest

# Run with Maven
mvn javafx:run
```

**Or simply open in IntelliJ IDEA and run `Main.java`**

> 💡 **Zero Configuration** — The SQLite database auto-creates on first run with all tables and seed data. No manual setup required!

### Test Credentials

| Role | Email | Password |
|------|-------|----------|
| Customer | `ziad@gmail.com` | `123` |
| Customer | `hamdy@gmail.com` | `123` |
| Merchant | `ahmed@zest.com` | `123` |

---

## 🏗️ Architecture

### Design Patterns Implemented

| Pattern | Implementation | Purpose |
|---------|---------------|---------|
| **Singleton** | `DBConnection`, `CartManager`, `Platform` | Centralized state management |
| **Factory** | `UserFactory` | Dynamic user type creation |
| **Strategy** | `PaymentStrategy` | Flexible payment method handling |
| **Adapter** | `PayPalAdapter` | Third-party payment integration |
| **Decorator** | `ToppingDecorator`, `ExtraCheese` | Dynamic menu item customization |
| **Snapshot** | `OrderItem` | Price preservation at purchase time |

### Project Structure

```
zest/
├── src/main/java/com/zest/
│   ├── controller/     # JavaFX Controllers
│   ├── dao/            # Data Access Layer
│   ├── logic/          # Business Logic & Patterns
│   ├── model/          # Domain Models
│   └── util/           # Utilities
├── src/main/resources/
│   ├── fxml/           # UI Layouts
│   └── images/         # Assets
└── pom.xml
```

### Class Hierarchy

```
User (Abstract)
├── Customer — Order history, reviews, order placement
└── Merchant — Menu management, order processing

Order
├── OrderItem — Snapshot pattern for price preservation
└── GroupOrder — Bill splitting functionality

MenuItem
└── ToppingDecorator (Abstract)
    └── ExtraCheese — Decorator pattern implementation
```

---

## 🛠️ Tech Stack

- **Language:** Java 17+
- **UI Framework:** JavaFX 17.0.2
- **Database:** SQLite with JDBC
- **Build Tool:** Maven
- **IDE:** IntelliJ IDEA (recommended)

---

## 📋 Prerequisites

- Java JDK 17 or higher
- Maven 3.6+ (or use included wrapper)
- IntelliJ IDEA (optional, but recommended)

---

## 🎯 Key Highlights

- **Role-Based Access Control** — Separate flows for customers and merchants
- **Secure Authentication** — SHA-256 password hashing
- **Automatic Schema Migration** — Database updates seamlessly between versions
- **Responsive UI** — Modern JavaFX interface with FXML layouts
- **Clean Architecture** — MVC pattern with clear separation of concerns

---

## 👥 Team

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/ahmed-hesham07">
        <b>Ahmed Hesham</b>
      </a>
      <br />
      <sub>Backend Architect & Database</sub>
    </td>
    <td align="center">
      <a href="https://github.com/XHamdy">
        <b>Hamdy Elmaghraby</b>
      </a>
      <br />
      <sub>Customer Experience & Menu Logic</sub>
    </td>
    <td align="center">
      <a href="https://github.com/ziad-135">
        <b>Ziad Osama</b>
      </a>
      <br />
      <sub>User Management & Order History</sub>
    </td>
  </tr>
</table>

---

## 📄 License

This project was developed as part of a Java Programming course.

---

<p align="center">
  Made with ☕ and Java
</p>
