import java.util.Scanner;
import java.sql.*;

public class BookstoreApp {
    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/toy_database", "postgres", "student");
             Statement statement = connection.createStatement();
             )
        {
            main_menu(statement);
        }
        catch (Exception sqle) {
            System.out.println("SQL Exception: " + sqle);
        }
    }

    public static void main_menu(Statement statement) throws SQLException {

        System.out.println("Welcome to Look Inna Book!\n");
        while (true) {
            System.out.print(
                    "Main Menu\n"
                    + "---------\n"
                    + "1. Customer Login\n"
                    + "2. Customer Sign up\n"
                    + "3. Owner Login\n"
                    + "4. Quit\n\n"
                    + "Please select an option (type in the corresponding number): ");

            String choice = scan.nextLine();

            if (choice.equals("1"))
                login_menu("customer");
            else if (choice.equals("2"))
                System.out.println(2);
            else if (choice.equals("3"))
                login_menu("owner");
            else if (choice.equals("4"))
                break;
            else
                System.out.println("\nInvalid input... please try again.\n");
        }
        System.out.println("\nThanks for stopping by!");
    }

    public static void login_menu(String userType) {


        System.out.print("\nusername: ");
        String username = scan.nextLine();
        System.out.print("password: ");
        String password = scan.nextLine();
        if (valid_credentials(userType, username, password)) {
            if (userType.equals("customer"))
                CustomerMenu.run(username);
            else
                OwnerMenu.run(username);
        }
        else
            System.out.println("\nInvalid Credentials... returning to main menu.\n");
    }

    public static boolean valid_credentials(String userType, String username, String password) {
        return false;
    }


}
