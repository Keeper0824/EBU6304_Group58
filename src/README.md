# Bank Card Manager Application

### **Developer**: @jmmboy (Yudian Wang)

A Java Swing application for managing bank card information with secure input validation and data handling features.

---

## Features

- **Secure Input Validation**
   - 16-digit card numbers with auto-formatting
   - Full name validation for cardholders
   - MM/YY expiry date format enforcement
   - 3-digit CVV verification

- **Data Management**
   - Table display of stored cards
   - Safe deletion with confirmation dialog
   - Non-editable table to prevent accidental changes

- **User-Friendly Interface**
   - Clean input form with real-time validation
   - Visual feedback for invalid input
   - Responsive table interactions

---

## System Requirements

- Java Runtime Environment (JRE) 8 or higher
- Minimum screen resolution: 1024×768

---

## Installation & Usage

### Quick Start
```bash
git clone [repository-url]
cd bank-card-manager
javac *.java
java BankCardManager
```

### Step-by-Step Guide

1. **Compilation**
   ```bash
   javac BankCardManager.java AddCardListener.java DeleteCardListener.java
   ```

2. **Execution**
   ```bash
   java BankCardManager
   ```

3. **Application Flow**
   - Enter card information → Click "Add Card" to save
   - Select a table row → Click "Delete Selected Card" to remove
   - Card numbers auto-format as you type (e.g., `1234 5678 9012 3456`)

---

## Validation Rules

| Field        | Rule                                  | Example           |
|--------------|----------------------------------------|-------------------|
| Card Number  | 16-digit number                        | 4111111111111111  |
| Card Holder  | Full English name (at least two words) | JOHN DOE          |
| Expiry Date  | MM/YY format (months 01–12, any year)  | 12/25             |
| CVV          | 3-digit number                         | 123               |

---

## Code Architecture

### Core Components

| File                    | Responsibility                            |
|-------------------------|-------------------------------------------|
| `BankCardManager.java`  | Main GUI layout and component initialization |
| `AddCardListener.java`  | Handles add-card logic and input validation |
| `DeleteCardListener.java` | Manages delete operations and confirmation dialogs |

### Design Principles

- **Single Responsibility Principle** – Each class focuses on a single function
- **Dependency Injection** – Components are passed via constructors
- **Thread Safety** – Uses the Event Dispatch Thread (EDT) for UI updates

---

## Notes

1. **Data Persistence**
   - Currently, the application does **not** support data persistence. All data is lost when the program is closed.

2. **Security Warning**
   - This is for **demonstration purposes only**. Do **not** store real card data.

3. **Extension Suggestions**
   - Add database support (e.g., SQLite or MySQL)
   - Implement encryption for sensitive fields
   - Add export functionality (CSV or Excel)

---

## License

[MIT License](LICENSE) © 2023 [Your Name]

---

**Developer Note**: Recommended to use JDK 11+ for development. IntelliJ IDEA is ideal for visual GUI debugging and layout tuning.



