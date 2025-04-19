# EBU6304_Group58 – Software Engineering Group Project
AI Empowered Personal Finance Tracker(Software Development Using Agile Methods)


## Feature #9: GUI Layout

- **UI Type**: Java Swing
- **Components**:
    - Text fields for user input
    - Table to display stored card entries
    - Buttons for adding and deleting cards
- Responsive and user-friendly layout
- Non-editable table to prevent accidental changes
- **Developer**:  @jmmboy (Yudian Wang)


## Feature #12: Card Management (Add, Delete & Validation)

- **Classes**: `BankCardManager`, `AddCardListener`, `DeleteCardListener`
- **Functions**:
    - Add new card entries to a table with real-time input validation
    - Delete selected card with confirmation prompt
    - Auto-format 16-digit card numbers (e.g., `1234 5678 9012 3456`)
    - Prevent accidental edits using a non-editable table

- **Validation Rules**:
    - **Card Number**: 16 digits only (e.g., `4111111111111111`)
    - **Card Holder**: Full name, at least two words (e.g., `JOHN DOE`)
    - **Expiry Date**: MM/YY format (e.g., `12/25`)
    - **CVV**: 3-digit number (e.g., `123`)

- Fully tested with real-time input handling
- **Developer**:  @jmmboy (Yudian Wang)


## Feature #14: View Membership Expiry Time

- **Classes**: `User`, `UserLoader`, `UserPanel`
- **Data**: `data/users.csv`
- **Description**:
    - Displays the expiry date and remaining days of premium membership.
    - Highlights users who have expired memberships.
-  Verified with JUnit unit testing.
- **Developer**: @Keeper0824 (Haoran Sun)


## Feature #15: User Management – Add New User

- **Classes**: `CashFlowController`, `CashFlowView`, `Transaction`
- **Data**: `data/users.csv`
- **Description**:
    - `Transaction` class: Stores transaction type (`income` or `expense`) and amount.
    - `CashFlowController` class: Loads transaction data and updates visual output:
        - Loads transaction records from CSV file.
        - Animates transaction display with 2-second intervals (simulated real-time effect).
        - Updates income, expense, and net cash flow displays.
        - Uses `PieChart` and `BarChart` to visualize income vs. expense ratios and monthly cash flow trends.
- Tested with JUnit to ensure new user functionality works correctly.  
  It successfully reads income/expense information from `InOutcome.csv` and renders analysis charts.
- **Developer**: @Keeper0824 (HaoRan Sun)
