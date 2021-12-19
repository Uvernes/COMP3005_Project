import javax.swing.plaf.nimbus.State;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;

public class MainMenu extends Menu {

    public MainMenu(Connection connection) {
        super(connection);
    }

    public void run() throws SQLException {

        System.out.println("Welcome to Look Inna Book!");
        while (true) {
            System.out.print(
                    "\nMain Menu\n"
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
                customer_registration();
            else if (choice.equals("3"))
                login_menu("owner");
            else if (choice.equals("4"))
                break;
            else
                System.out.println("\nInvalid input... please try again.");
        }
        System.out.println("\nThanks for stopping by!");
    }

    public void login_menu(String userType) throws SQLException {

        System.out.print("\nusername: ");
        String username = scan.nextLine();
        System.out.print("password: ");
        String password = scan.nextLine();
        if (valid_credentials(userType, username, password)) {
            if (userType.equals("customer"))
                (new CustomerMenu(connection, username)).run();
            else
                (new OwnerMenu(connection, username)).run();
        }
        else
            System.out.println("\nInvalid Credentials... returning to main menu.");
    }

    public boolean valid_credentials(String userType, String username, String password) throws SQLException {

        String query = "select password from user_account where username = ? and username in (select username from #)";
        query = query.replace("#", userType);
        PreparedStatement checkCredentials = connection.prepareStatement(query);
        checkCredentials.setString(1, username);
        ResultSet rset = checkCredentials.executeQuery();
        if (rset.next()) {
            System.out.println(rset.getString("password"));
            return password.equals(rset.getString("password"));
        }
        // If we get to here, it means empty query was returned
        return false;
    }

    public void customer_registration() throws SQLException {
        System.out.println("\nCustomer registration:");
        System.out.println("----------------------");
        System.out.print("username: ");
        String username = scan.nextLine().toLowerCase();
        if (QueryUtilityFunctions.username_taken(connection, username)) {
            System.out.println("\nUsername taken... returning to main menu.\n");
            return;
        }
        System.out.print("password: ");
        String password = scan.nextLine().toLowerCase();
        System.out.print("First name: ");
        String first_name = scan.nextLine().toLowerCase();
        System.out.print("last name: ");
        String last_name = scan.nextLine().toLowerCase();
        System.out.print("Email address: ");
        String email = scan.nextLine().toLowerCase();
        System.out.print("Credit card number: ");
        String credit_card_number = scan.nextLine().toLowerCase();

        // Insert into address, area tables as necessary
        String[] shipping_address = QueryUtilityFunctions.address_registration(connection, "shipping");
        String[] billing_address = QueryUtilityFunctions.address_registration(connection, "billing");

        String shipping_postal_code = shipping_address[0];
        String shipping_street_address = shipping_address[1];
        String billing_postal_code = billing_address[0];
        String billing_street_address = billing_address[1];

        // Insert into user and customer tables
        QueryUtilityFunctions.insert_into_table(
                connection, "user_account", new String[]{"string", "string", "string", "string", "string"},
                new String[]{username, password, first_name, last_name, email});

        QueryUtilityFunctions.insert_into_table(
                connection, "customer", new String[]{"string", "string", "string", "string", "string", "string"},
                new String[]{username, credit_card_number, billing_postal_code,
                billing_street_address, shipping_postal_code, shipping_street_address});

        System.out.println("\nCustomer account created!");
    }
}
