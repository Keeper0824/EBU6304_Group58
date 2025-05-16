# EBU6304_Group58 – Software Engineering Group Project
AI Empowered Personal Finance Tracker(Software Development Using Agile Methods)

## Feature #1:Open Account
- **Class**: `RegistrationApp`
- **Data**: `data/user.csv`
- **Functions**:
  Create an account for the user, get the email address, nickname, gender, birthday
  Ability to store user information in a CSV file
  Once the registration is complete, you will be automatically returned to the login screen
- Tested with JUnit
- Developer: @lshmy(Muchi Wei)

## Feature #2:Add or Modify User Information
- **Class**: `UserManagementApp`
- **Data**: `data/user.csv`
- **Functions**:
  You can jump to the Modify Information page on the user interface, and users can view their current information
  Users can modify their information below, but the email address cannot be modified as a login credential for the time being
- Tested with JUnit
- **Developer**: @lshmy(Muchi Wei)

## Feature #3: Security Verification

- **Classes**: `LoginApp`, `LoginConController`, `RegistrationApp`, `RegistrationController`, `User`
- **Data**: `data/user.csv`
- **Description**:
  Enforces multi-factor authentication (password + verification code) during login.
  Applies SHA-256 hashing to user passwords during registration.
- Tested with JUnit to ensure the user can login successfully with password and verification code. The password in csv file are all encrypted.
- **Developer**: @hzxuan6628(Zhengxuan Han)

## Feature #4: User Registration
- **Class**: `UserSearchApp` and `UserSearchController`
- **Data**: `data/user.csv`
- **Description**:
  Implements a user search functionality where users can be searched by their nickname.
  Loads user data (nickname, email, gender, date of birth) from a CSV file during initialization.
  Displays user information in a dialog box when a matching nickname is found.
  If no matching user is found, an informational alert is shown.
- Tested with JUnit to ensure correct user search functionality by nickname. The user data is loaded from an encrypted CSV file.
- **Developer**: @xiaodonx(Kaiyu Liu)

## Feature #5: Import Consumption Data

- **Classes**: `Transaction`, `mainApp`, `addController`
- **Data**: `data/transaction.csv`
- **Description**:
  It can add consumption data, including Transaction, Price, Classification and Date.
  The data can be saved in a csv file and used for analysis.
- Verified with JUnit unit testing.
- **Developer**: @hzxuan6628(Zhengxuan Han)

## Feature #6: Classify Transactions

- **Class**: `Transaction`, `addController`
- **Data**: `data/transaction.csv`
- **Functions**:
  Collecting transaction data, get Transaction, Price, Classification, Date
  Ability to store transaction information in a CSV file
  Once the collection is complete, you will be automatically returned to the form and refresh
- **Developer**: @Wei13461(Wei Chen)

## Feature #7: View Categories

- **Class**: `mainApp`, `mainController`, `Transaction`
- **Data**: `data/transaction.csv`
- **Functions**:
  Load data from transaction csv file
  Use a form to show different attributes of the transaction data
  Provide buttons to edit, delete or add data
  Save changes to csv file
- Tested with JUnit
- **Developer**: @Wei13461(Wei Chen)

## Feature #8: Expense Editing

- **Class**: `Transaction`, `editController`
- **Data**: `data/transaction.csv`
- **Functions**:
  Show the current data of certain transaction
  Collecting the changed transaction data
  Ability to store transaction information in a CSV file
  Once the collection is complete, you will be automatically returned to the form and refresh
- Tested with JUnit
- **Developer**:@Wei13461(Wei Chen)

## Feature #9: GUI Layout

- **UI Type**: Java Swing
- **Components**:
    - Text fields for user input
    - Table to display stored card entries
    - Buttons for adding and deleting cards
- **Responsive and User-Friendly Layout**
- **Read-Only Table**: Prevents accidental modifications
- **Developer**: @jmmboy (Yudian Wang)

## Feature #10: Display of Member Benefits

- **Classes**: `User`, `UserController`, `UserLoader`, `Main`
- **Data**: `data/user.csv`
- **Description**:
  This feature provides a comprehensive overview of all the rights and interests that members enjoy. It clearly lists various benefits such as exclusive deals, priority support, early 
  access. Non - members can access this information to make an informed decision on whether to join the membership.
- Verified with JUnit unit testing.
- **Developer**: @hzxuan6628(Zhengxuan Han)

## Feature #11: Consumption habit Forecasting
- **Class**: ConsumptionForecastController, AIModelAPI, DataPreprocessor
- **Data**: data/{nickname}_transaction.csv
- **Description**:
  Utilizes machine learning algorithms to predict the next month's expenses based on historical transaction data and seasonal consumption patterns (e.g., Singles' Day).
  Loads user transaction data from a CSV file, preprocesses the data, and sends it to the AI model API for prediction.
  The AI model identifies and prioritizes seasonal events in historical data to provide accurate expense forecasts.
  Ensures VIP users' expense predictions have an error rate of ≤15%.
  If insufficient seasonal data exists for prediction, the system displays a warning and provides a conservative default estimate.
  Displays the predicted expense amount to the user.
- Verified with JUnit unit testing.
- **Developer**: @lshmy(Muchi Wei)

## Feature #12: Card Management (Add, Delete & Validation)

- **Classes**: `BankCardManager`, `AddCardListener`, `DeleteCardListener`

- **Functions**:
    - Add new card entries to the table with real-time input validation
    - Delete selected cards with a confirmation prompt
    - Auto-format 16-digit card numbers (e.g., `1234 5678 9012 3456`)
    - Use a read-only table to prevent accidental edits

- **Validation Rules**:
    - **Card Number**: 16 digits only (e.g., `4111111111111111`)
    - **Card Holder**: Full name, at least two words (e.g., `JOHN DOE`)
    - **Expiry Date**: MM/YY format (e.g., `12/25`)
    - **CVV**: 3-digit number (e.g., `123`)

- **Testing**: Fully tested with real-time input handling
- **Developer**: @jmmboy (Yudian Wang)

## Feature #13: View Categories
- **Class**: `UserSearchApp` and `UserSearchController`
- **Data**: `data/user.csv`
- **Description**:
  Allows users to search by nickname, and displays user details (nickname, email, gender, date of birth).
  Supports searching for VIP and Normal users by entering "VIP" or "normal".
  Stores user data in a CSV file and loads it into memory on initialization.
- Tested with JUnit to ensure correct functionality of user search by nickname. The user data is stored in the CSV file.
- **Developer**: @xiaodonx(Kaiyu Liu)

## Feature #14: View Membership Expiry Time

- **Classes**: `User`, `UserLoader`, `UserController`
- **Data**: `data/user.csv`
- **Description**:
    - Displays the expiry date and remaining days of premium membership.
    - Highlights users who have expired memberships.
-  Verified with JUnit unit testing.
- **Developer**: @Keeper0824 (Haoran Sun)

## Feature #15: User Management – Add New User

- **Classes**: `CashFlowController`, `CashFlowView`, `Transaction`
- **Data**: `data/user.csv`
- **Description**:
    - `Transaction` class: Stores transaction type (`income` or `expense`) and amount.
    - `CashFlowController` class: Loads transaction data and updates visual output:
        - Loads transaction records from CSV file.
        - Animates transaction display with 2-second intervals (simulated real-time effect).
        - Updates income, expense, and net cash flow displays.
        - Uses `PieChart` and `BarChart` to visualize income vs. expense ratios and monthly cash flow trends.
- Tested with JUnit to ensure new user functionality works correctly.  
  It successfully reads income/expense information from `InOutcome.csv` and renders analysis charts.
- **Developer**: @Keeper0824 (Haoran Sun)

## Feature #16: Comparison of Budget with Actual Expenditure

- **Classes**: `Transaction`, `AnalysisReportController`，`MainApp`
- **Data**: `data/user.csv`
- **Description**:
    - AI model is introduced to analyze the transaction data over a period of time.
    - Open a new window to display the analysis report.
- Tested with JUnit to ensure the AI model grab the correct data from csv file and work operates effectively. 
- **Developer**: @hzxuan6628(Zhengxuan Han)

## Feature #17: Consumption Reduction Suggestion System
- **Classes**: `SuggestionController`
- **Data**: `data/user.csv`
- **Description**:
    - Uses an AI model to analyze user spending data and provide personalized cost-cutting suggestions.
    - Reads "expense" records from the user's CSV file, sends a prompt to the AI, and displays saving advice to help reduce unnecessary expenses.
-Tested with JUnit to Verify that the system correctly reads transaction data from CSV and interacts with the AI model as expected.
Developer: @xiaodonx (Kaiyu Liu)

## Feature #19: Reminder Application

* **Classes**:`Main`, `ScheduleItem`, `ScheduleListController`
* **Data**: `data/{currentUser}_reminders.csv` (and `data/user.csv` for VIP expiry)
* **Description:**
    - **Text fields** for user input
    - **Table (ListView)** to display stored reminder entries
    - **Buttons** for adding and deleting reminders
    - **Responsive Layout** with a user-friendly interface
    - **Read-Only Table** to prevent accidental modifications
* **Developer**: @jmmboy (Yudian Wang)


## Feature #20: Holiday Reminder

* **Classes**: `ScheduleListController`, `ScheduleItem`, `UserLoader`
* **Data**: `data/{currentUser}_reminders.csv` (and `data/user.csv` for VIP expiry)
* **Description:**
  The “Holiday Reminder” feature automatically loads any existing custom reminders when initialized and supplements them with today’s official holiday based on the current date (`LocalDate.now()`). For example, if today is “Test Day” (mapped to `MonthDay.of(5, 9)`), the system will automatically add a reminder titled **“Holiday Reminder”** with details like

```
Today is Test Day. Remember to arrange your budget reasonably.
```

* Verified with JUnit unit testing.
* **Developer**: @Keeper0824 (Haoran Sun)

## Feature #21: Financial Health Scoring

- **Class:** ConsumptionHealthAnalyzer
- **Data:** data/{nickname}_transaction.csv
- **Description:**
  - Calculates a monthly financial health score for the user based on factors like savings rate, debt ratio, and spending habits.
  - Analyzes transaction data to assess the user's financial well-being and provides a score from 0 to 100.
  - Generates actionable recommendations to help the user improve their financial health based on the score.
  - The recommendations are tailored to the user's specific financial situation and habits.
  - Displays the financial health score and corresponding recommendations to the user.
- Verified with JUnit unit testing.
- **Developer:** @lshmy(Muchi Wei)

## Feature #24: MainMenu Navigation

- **Classes**: `MainMenuApp`, `MainMenuController`, `UserManagementApp`, `UserManagementController`, `User`
- **Displays** a centralized navigation interface with clear and accessible buttons for different functional modules, including:
- **User Information**: View and edit account information
- **VIP Center**: Check premium membership status and benefits
- **Charts**: Visualize financial reports and trends
- **Cards**: Manage payment bank cards
- **Transaction**: View and add transaction
- **Logout**: Securely exit the current account
- **Clicking** different buttons can lead to interfaces with different functions, and there is a back button to return to the main menu.
- **Verified** with JUnit unit testing.
- **Developer**: @hzxuan6628(Zhengxuan Han)



## Feature #25: VIP Membership Recharge
* **Classes**: `Main`, `RechargePopupController`, `User`, `UserController`, `UserLoader`
* **Data**: `data/user.csv`
* **Description:**
- **UI Type**: JavaFX (with FXML)
- **Components**:
- **Text fields** for displaying user data such as VIP status and expiry date.
- **Buttons** for opening the recharge popup and managing membership.
- **Popup Window** for selecting the VIP membership duration (1 month, 3 months, or 12 months).
- **Label** for displaying VIP status and remaining days until membership expiry.
- **Responsive Layout** with a user-friendly interface for managing and updating VIP memberships.
- **Developer**: @jmmboy (Yudian Wang)**



## Feature #26: Financial Analysis System
* **Classes**: `MainApp`, `FinancialController`
* **Data**: `data/transaction.csv`
* **Description:**  
  The Financial Analysis System provides a graphical interface for tracking financial transactions, displaying income and expenses. The system loads transaction data from a CSV file and presents it in pie and bar charts.  
- **Pie Chart**: Displays expenses by category.
- **Bar Chart**: Shows the trend of monthly income vs. expenses.
- **UI Enhancements**: The background features a gradient, and the charts are styled for better readability.
- **Error Handling**: Displays error messages if the data cannot be loaded or processed correctly.
- **Developer**: @jmmboy (Yudian Wang)**
