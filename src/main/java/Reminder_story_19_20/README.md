# Reminder Application - A JavaFX Reminder Management Tool

This Java application serves as a reminder management system that provides users with notifications for various types of reminders. The primary use cases include displaying holiday reminders and tracking VIP membership expirations. The application utilizes JavaFX for the user interface, integrates with CSV files for persistent data storage, and provides dynamic updates for VIP expiry status.

## Features
- **Reminder Management:**
    - Users can view and manage a list of their scheduled reminders.
    - Each reminder can contain a title, a detailed description, and a due date.
    - The reminders are displayed in a clean, user-friendly interface with the ability to mark them as complete.

- **Holiday Reminders:**
    - The application automatically adds holiday reminders based on the current date.
    - The system recognizes and adds a fixed set of holidays like New Year’s Day, Valentine’s Day, and more.
    - Each holiday reminder includes a default message, reminding the user of the significance of the day.

- **VIP Expiry Notifications:**
    - The system regularly checks the expiry status of a VIP membership every 5 seconds.
    - If a VIP membership is set to expire within the next 7 days, a reminder is created for the user.
    - Notifications prompt the user to renew their membership before it expires.

- **CSV File Integration:**
    - The reminders are stored in CSV files to ensure persistent data storage.
    - New reminders are added to the CSV automatically and can be loaded on application startup.
    - This provides a straightforward way of managing and exporting reminder data.

- **Dynamic List Updates:**
    - The reminder list is continuously updated to ensure that only the latest 10 reminders are shown.
    - Reminders are sorted by date and title, with the most recent ones appearing at the top.

## File Descriptions

### 1. `Main.java`
- **Description:** This is the main entry point of the application, where the JavaFX application is launched.
- **Responsibilities:**
    - **Application Initialization:**
        - The `start` method loads the main user interface (ReminderView.fxml), applies the corresponding CSS stylesheet, and sets up the primary stage.
        - It is the entry point that initializes the user interface when the application is started.
    - **Launching the Application:**
        - The `main` method calls the `launch()` function to start the JavaFX application.

- **Key Components:**
    - `FXMLLoader.load(...)` loads the user interface from the FXML file.
    - `Scene` sets the user interface layout and applies styles from the CSS.
    - `primaryStage.setScene(scene)` sets the scene on the primary stage and makes it visible to the user.

---

### 2. `ScheduleItem.java`
- **Description:** This class models a reminder item. It holds the data related to a single reminder, including the date, title, detailed description, and completion status.
- **Responsibilities:**
    - **Properties:**
        - `date`: Stores the reminder's due date (using `LocalDate`).
        - `title`: A short summary or title for the reminder.
        - `details`: A list of additional details related to the reminder.
        - `done`: A boolean that indicates whether the reminder has been completed.
    - **Methods:**
        - `getDate()`, `getTitle()`, `getDetails()`: Getter methods to retrieve the reminder's properties.
        - `isDone()` and `setDone()`: Methods to check and set the reminder's completion status.
    - **Constructor:**
        - The constructor allows for the initialization of a `ScheduleItem` object with the date, title, and details.

- **Usage:**
    - `ScheduleItem` objects are used to represent each reminder and are stored in the application’s reminder list (`ObservableList<ScheduleItem>`).

---

### 3. `ScheduleListController.java`
- **Description:** This is the controller class for managing the reminder view. It controls the reminder list, interacts with the CSV data, and handles periodic tasks such as checking VIP expiry status.
- **Responsibilities:**
    - **Loading and Displaying Reminders:**
        - The `initialize()` method loads reminders from the CSV file, adds today’s holiday reminders if applicable, and sets up the reminder list view.
        - The `updateScheduleList()` method updates the display to show the most recent reminders, sorted by date.
    - **VIP Expiry Notifications:**
        - The `startScheduledTask()` method uses a `Timeline` to periodically check for VIP expirations every 5 seconds.
        - When a VIP membership is within 7 days of expiry, a new reminder is added, and the user is notified.
    - **CSV Data Handling:**
        - The `appendCsv()` method appends new reminder data to the user's CSV file to maintain persistent data.
        - The `loadCsvAndHoliday()` method reads the CSV file and adds any existing reminders. It also checks if today is a holiday and adds it to the reminder list.
    - **ListView Management:**
        - The `initListView()` method configures the `ListView` to display `ScheduleItem` objects, using a custom cell factory to style the items.
    - **Holiday Reminders:**
        - The `HOLIDAYS` map contains a predefined set of holidays (e.g., New Year's Day, Valentine's Day). If today matches one of these dates, the system adds a holiday reminder automatically.

- **Key Components:**
    - **CSV Handling:** `BufferedReader` and `BufferedWriter` are used to read from and write to the CSV file.
    - **VIP Checking:** The application periodically checks for VIP expiry dates by reading data from the `user.csv` file.
    - **ListView Rendering:** The `ListCell` class is customized to render the reminder details as styled cards within the list view.

---

## Setup Instructions

1. **Clone or Download the Repository:**
    - Download or clone the repository to your local machine to access the source code.

2. **Install Java Development Kit (JDK):**
    - Ensure that Java JDK version 8 or higher is installed. You can download it from the official Oracle website or use a package manager for installation.

3. **Running the Application:**
    - Open the project in your preferred Java IDE (e.g., IntelliJ IDEA, Eclipse).
    - Alternatively, from the command line, navigate to the project directory and compile the files:
      ```bash
      javac -d bin src/main/java/Reminder_story_19_20/*.java
      ```
    - Run the application:
      ```bash
      java -cp bin Reminder_story_19_20.Main
      ```

4. **CSV Data Storage:**
    - The reminders are saved in CSV files located in the `data/` directory. These CSV files are used to persist the user’s data between sessions.
    - The file format is as follows:
      ```
      YYYY-MM-DD, Category, Reminder Detail
      ```

---

## Dependencies
- **JavaFX:** The application uses JavaFX for the graphical user interface. Make sure you have JavaFX properly configured if you are running this outside of an IDE.
- **Java 8 or Later:** The code requires Java 8 or later, as it uses features such as lambda expressions and the `java.time` API.

---

## Future Enhancements
- **Integrate Calendar APIs:** Incorporate external calendar APIs to fetch and manage holidays or special dates.
- **Improve UI:** Enhance the user interface with more complex interactions, animations, and more advanced controls.
- **Notifications System:** Implement a system that can send desktop notifications or email reminders.
- **Multi-User Support:** Allow multiple users to have personalized reminder lists and data.

---

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

## Authors
- **Haoran Sun**: Main Developer and Contributor
- **Yudian Wang**: Contributor (VIP Expiry Feature)

---

## Acknowledgments
- JavaFX and Java 8 for the platform and features used in this application.
- The open-source community for continuous support and resources.

