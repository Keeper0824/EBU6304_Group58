import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class CustomerRegistrationSystem {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Customer Account Registration ===");
        
        // Collect customer information
        String nickname = getValidInput(scanner, "Enter nickname: ", input -> !input.trim().isEmpty(), 
            "Nickname cannot be empty");
        
        String password = getValidInput(scanner, "Enter password: ", input -> input.length() >= 8, 
            "Password must be at least 8 characters long");
        
        String email = getValidInput(scanner, "Enter email: ", input -> input.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$"), 
            "Invalid email format");
        
        String gender = getValidInput(scanner, "Enter gender (M/F/O): ", 
            input -> input.equalsIgnoreCase("M") || input.equalsIgnoreCase("F") || input.equalsIgnoreCase("O"), 
            "Gender must be M (Male), F (Female), or O (Other)").toUpperCase();
        
        LocalDate dob = getValidDateInput(scanner, "Enter date of birth (YYYY-MM-DD): ");
        
        // Create customer object
        Customer customer = new Customer(nickname, password, email, gender, dob);
        
        // Display registration summary
        System.out.println("\n=== Registration Summary ===");
        System.out.println(customer);
        
        // Here you would typically save the customer to a database
        // saveCustomerToDatabase(customer);
        
        scanner.close();
    }
    
    private static String getValidInput(Scanner scanner, String prompt, 
                                      Validator validator, String errorMessage) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine();
            if (!validator.validate(input)) {
                System.out.println("Error: " + errorMessage);
            }
        } while (!validator.validate(input));
        return input;
    }
    
    private static LocalDate getValidDateInput(Scanner scanner, String prompt) {
        LocalDate date = null;
        boolean valid = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        do {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                date = LocalDate.parse(input, formatter);
                valid = true;
            } catch (DateTimeParseException e) {
                System.out.println("Error: Invalid date format. Please use YYYY-MM-DD.");
            }
        } while (!valid);
        
        return date;
    }
    
    @FunctionalInterface
    interface Validator {
        boolean validate(String input);
    }
}

class Customer {
    private String nickname;
    private String password; // In real application, this should be encrypted
    private String email;
    private String gender; // M, F, or O
    private LocalDate dateOfBirth;
    
    public Customer(String nickname, String password, String email, String gender, LocalDate dateOfBirth) {
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }
    
    // Getters
    public String getNickname() { return nickname; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getGender() { return gender; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    
    @Override
    public String toString() {
        return "Customer Details:\n" +
               "Nickname: " + nickname + "\n" +
               "Email: " + email + "\n" +
               "Gender: " + gender + "\n" +
               "Date of Birth: " + dateOfBirth + "\n";
    }
}


// shenadaudiuafbaufiya
//cd E:/aStudy/newjava/EBU6304_Group58  