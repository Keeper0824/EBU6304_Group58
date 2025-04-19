# EBU6304_Group58 – Software Engineering Group Project
AI Empowered Personal Finance Tracker(Software Development Using Agile Methods)

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
