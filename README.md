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