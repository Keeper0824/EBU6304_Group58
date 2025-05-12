package src.main.java.Login_story1_3;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import src.main.java.Session;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Title      : UserManagementController.java
 * Description: Controller for the user management view.
 *              It initializes the form with user data, handles form updates,
 *              and provides navigation back to the main menu.
 *
 * @author Zhengxuan Han
 * @version 1.0
 */

public class UserManagementController {
    @FXML private TextField nicknameField;
    @FXML private TextField emailField;
    @FXML private ChoiceBox<String> genderChoiceBox;
    @FXML private DatePicker dobPicker;

    private static final String sessionNickname = Session.getCurrentNickname();
    private User currentUser;

    /**
     * Initializes the form.
     * 1. Initializes the gender options in the choice box.
     * 2. Loads all users from the CSV file and finds the current user based on the session nickname.
     * 3. If the user is found, updates the form fields with the user's information; otherwise, shows an error alert.
     */
    @FXML
    private void initialize() {
        // 1. Initialize gender options
        genderChoiceBox.getItems().addAll("Male", "Female", "Other");

        // 2. Load users from CSV and find the current user
        List<User> all = loadUsersFromCSV();
        for (User u : all) {
            if (sessionNickname.equals(u.getNickname())) {
                currentUser = u;
                break;
            }
        }

        // 3. If the user is found, update the form fields
        if (currentUser != null) {
            updateFormFields();
        } else {
            showAlert("Error", "No user found for: " + sessionNickname);
        }
    }

    private Runnable returnToMainMenuCallback;

//    /**
//     * Set the callback for returning to the main menu.
//     *
//     * @param callback The callback function.
//     */
//    public void setReturnToMainMenuCallback(Runnable callback) {
//        this.returnToMainMenuCallback = callback;
//    }
//
//    /**
//     * Handles the back button click event.
//     * Closes the current window and executes the callback to return to the main menu.
//     */
//    @FXML
//    private void handleBack() {
//        if (returnToMainMenuCallback != null) {
//            // Close the current window
//            Stage stage = (Stage) nicknameField.getScene().getWindow();
//            stage.close();
//
//            // Execute the callback to open the main menu
//            returnToMainMenuCallback.run();
//        }
//    }

    /**
     * Set the current user.
     * Runs the updateFormFields method on the JavaFX application thread.
     *
     * @param user The user object.
     */
    public void setUser(User user) {
        this.currentUser = user;
        Platform.runLater(this::updateFormFields);
    }

    /**
     * Updates the form fields with the current user's information.
     * Handles potential date parsing exceptions and shows an error alert if necessary.
     */
    private void updateFormFields() {
        try {
            if (currentUser != null) {
                nicknameField.setText(currentUser.getNickname());
                emailField.setText(currentUser.getEmail());
                genderChoiceBox.setValue(currentUser.getGender());

                // Safely parse the date
                if (currentUser.getDateOfBirth() != null && !currentUser.getDateOfBirth().isEmpty()) {
                    dobPicker.setValue(LocalDate.parse(currentUser.getDateOfBirth()));
                }
            }
        } catch (DateTimeParseException e) {
            showAlert("Error", "Invalid date format: " + currentUser.getDateOfBirth());
            dobPicker.setValue(null);
        }
    }

    /**
     * Handles the update button click event.
     * Validates the form fields, updates the user information, saves the changes to the CSV file,
     * and shows a success or error alert accordingly.
     */
    @FXML
    private void handleUpdate() {
        try {
            // Validate fields
            if (nicknameField.getText().isEmpty() ||
                    genderChoiceBox.getValue() == null ||
                    dobPicker.getValue() == null) {
                showAlert("Error", "All fields are required!");
                return;
            }

            // Update user information
            currentUser.setNickname(nicknameField.getText());
            currentUser.setGender(genderChoiceBox.getValue());
            currentUser.setDateOfBirth(dobPicker.getValue().toString());

            // Save changes
            saveUserChanges(currentUser);

            showAlert("Success", "User updated successfully!");

        } catch (Exception e) {
            showAlert("Error", "Update failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Saves the user changes to the CSV file.
     * Loads all users from the CSV, updates the corresponding user information,
     * and then saves all users back to the CSV.
     *
     * @param user The user object with updated information.
     */
    private void saveUserChanges(User user) {
        try {
            List<User> users = loadUsersFromCSV();
            boolean found = false;
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getEmail().equals(user.getEmail())) {
                    users.set(i, user);
                    found = true;
                    break;
                }
            }

            if (!found) {
                users.add(user);
            }

            saveUsersToCSV(users);

            System.out.println("User changes saved successfully");
        } catch (Exception e) {
            throw new RuntimeException("Failed to save user changes", e);
        }
    }

    /**
     * Loads all users from the CSV file.
     * Parses each line of the CSV and creates User objects.
     *
     * @return A list of User objects.
     */
    private List<User> loadUsersFromCSV() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data/user.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 8) {
                    User user = new User(data[0], data[1], data[2], data[3], data[4],data[5],data[6],data[7]);
                    users.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load users from CSV.");
        }
        return users;
    }

    /**
     * Saves a list of users to the CSV file.
     * Converts each user object to a CSV line and writes them to the file.
     *
     * @param users A list of User objects.
     */
    private void saveUsersToCSV(List<User> users) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/user.csv"))) {
            for (User user : users) {
                bw.write(String.join(",", user.getID(),user.getNickname(), user.getPassword(), user.getEmail(), user.getGender(), user.getDateOfBirth(),
                        user.getMembershipType(), user.getExpiryDate()));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save users to CSV.");
        }
    }

    /**
     * Displays an alert with the given title and message.
     * Calls the overloaded showAlert method with a null post-action.
     *
     * @param title The title of the alert.
     * @param message The message of the alert.
     */
    private void showAlert(String title, String message) {
        showAlert(title, message, null);
    }

    /**
     * Displays an alert with the given title, message, and an optional post-action.
     * Runs on the JavaFX application thread.
     *
     * @param title The title of the alert.
     * @param message The message of the alert.
     * @param postAction An optional callback function to be executed after the alert is closed.
     */
    private void showAlert(String title, String message, Runnable postAction) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);

            alert.showAndWait().ifPresent(response -> {
                if (postAction != null) {
                    postAction.run();
                }
            });
        });
    }
}