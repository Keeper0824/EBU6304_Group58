# EBU6304_Group58 – Software Engineering Group Project

## **AI Empowered Personal Finance Tracker - Beta v2.1**

This project is a modern desktop application designed to help users manage and visualize personal finances efficiently. It is developed using Java 21 and JavaFX, adopting agile development methodologies, and leverages third-party libraries such as Jackson (for JSON processing) and JUnit (for unit testing). The system features a modular, extensible architecture and a user-friendly graphical interface, supporting data persistence, categorized transactions, visual analytics, and reminder functions.

------

## System Requirements

### Hardware Requirements

- **Processor:** Intel i3 8th Gen or equivalent (minimum)
- **Memory:** 2 GB RAM (minimum), 4 GB or higher recommended
- **Disk:** At least 200 MB free storage space
- **Display:** 1600x900 resolution or higher recommended
- **Operating System:** Windows 10/11, macOS, or Linux (tested on Windows 10/11)

### Software Requirements

- **JDK 21** (Make sure Java is installed and the environment variables are correctly configured)
- **JavaFX SDK 21.0.7**
- **IDE:** IntelliJ IDEA is recommended (Eclipse or other modularity-supporting IDEs are also compatible)
- **Dependencies:** All required external libraries (e.g., jackson-core, jackson-databind, junit, etc.) are included in the `lib` directory.

------

## Directory Structure Overview

For new contributors, the project directory is structured as follows:

```
├── src/main/java           # Main source code (Java packages, controllers, models)
├── src/main/resources      # FXML, configuration, and static resources (stylesheets, images)
├── src/test/java           # Unit test source code
├── lib                     # Third-party library dependencies (.jar files)
├── out                     # Compilation output directory
├── docs                    # Project documentation and design reports
└── README.md               # This README file
```

------

## Project Setup and Execution

### 1. Configure JavaFX Runtime Parameters

Since JavaFX is no longer bundled with the JDK since version 11, you must specify the JavaFX SDK module path manually.

**VM Options Example (for Windows):**

```
--module-path "C:\Program Files\Java\javafx-sdk-21.0.7\lib" --add-modules javafx.controls,javafx.fxml
```

> Please adjust the path to match your actual JavaFX SDK installation.

### 2. Set the Working Directory

Ensure the working directory in your run configuration points to the project root, for example:

```
E:\Lessons\University\ThirdYear\Software Engineer\Coursework\EBU6304_Group58
```

### 3. Main Class

The main class to launch the application is:

```
src.main.java.Login_story1_3.LoginApp
```

Please verify that the package structure and class name match your actual code.

------

## Dependencies

The following external libraries are used and located in the `lib` folder:

- **Jackson Core, Annotations, Databind** (version 2.19.0-rc2)
- **JUnit Jupiter** 5.9.0 and its related dependencies

Please ensure all `.jar` files are correctly added to your project dependencies via your IDE.

------

## IDE Configuration Recommendations

Below is an example based on **IntelliJ IDEA**:

1. **Add Libraries via Maven:**
  - Go to **File > Project Structure > Libraries**.
  - Click the **+** button, select **From Maven**.
  - Search for `jackson-databind` and add `com.fasterxml.jackson.core:jackson-databind:2.19.0-rc2`.
  - Similarly, add `org.junit.jupiter:junit-jupiter:5.9.0`.
2. **JavaFX Setup:**
  - Open **Run > Edit Configurations**.
  - Create a new Java Application configuration, set the main class as required.
  - In the VM options field, add the previously mentioned module path option.
  - Save and apply the configuration.

------

## Compilation and Execution

- You can build and run the project directly using the IDE's "Run" button after the above configurations are complete.
- **Note:** Command-line execution is not supported by default due to JavaFX resource pathing issues. If you need to run via command-line, you must adjust the paths for FXML and CSS resources in the code to absolute or compatible relative paths.

------

## Unit Testing

The project uses **JUnit Jupiter** for unit tests.

- Test cases are located in `src/test/java`.
- All tests can be run directly from the IDE's built-in test runner.

------

## Version Control

- **Git** is used for version management.
- All team members are encouraged to follow standard git workflows:
  - Branch-based development (feature branches, bugfix branches, etc.)
  - Frequent commits with clear, concise messages
  - Pull requests for code review and merging
  - Resolve conflicts promptly and keep the main branch stable

------

## Known Issues and FAQ

- **FXML or CSS File Not Found:** If you encounter errors related to FXML or CSS not being found, ensure all resource paths are correct and files are present under `src/main/resources`.
- **Cannot Run via Command Line:** By default, this project is intended to be run from within an IDE. Command-line execution requires manual adjustment of FXML and CSS resource paths.
- **Module/Class Not Found Errors:** Double-check your module path and classpath settings, and make sure your Java and JavaFX versions match the requirements.
- **UI Occupies Full Screen:** If the application window appears oversized or occupies the entire screen, try disabling system display scaling or increasing your screen resolution.

------

## Code Style and Contribution Guidelines

- The project follows the **MVC (Model-View-Controller)** design pattern:
  - **Model:** POJO (JavaBean) classes for business/data logic (e.g., User, Transaction).
  - **View:** JavaFX FXML files and CSS for UI layout and styling.
  - **Controller:** Java classes annotated with `@FXML`, handling events, data binding, and communication between view and model.
- Additionally, the project architecture can be mapped to the **Boundary-Entity-Controller (BEC)** pattern:
  - **Boundary:** Represents the interface between the system and external actors, implemented as the FXML views and their associated controllers.
  - **Entity:** Refers to the core business objects and data structures, implemented as JavaBeans (POJOs) such as `User`, `Transaction`, etc.
  - **Controller:** Handles the application logic, orchestrates interactions between boundaries and entities, and may be further separated into service or use-case classes for complex business workflows.
- Code should be clean, well-documented, and use meaningful naming conventions.
- Please refer to the team documentation or contact the lead developer for further guidance.

------

### Contribution Guidelines

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


## Notes

- Ensure your Java version is 21.
- Verify that the JavaFX SDK version matches your JDK.
- For Windows, paths with spaces must be enclosed in double quotes.
- If you encounter "module not found" or "class not found" errors, check all configuration settings for correctness.

------

## Contact

- **Email:** [jp2022213462@qmul.ac.uk](mailto:jp2022213462@qmul.ac.uk)
- **Phone:** +86 157 2662 1095

------

If you need further help with configuration, contribution guidelines, or encounter any issues, please do not hesitate to reach out via email or phone.

------