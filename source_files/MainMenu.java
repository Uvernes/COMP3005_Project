import javax.swing.plaf.nimbus.State;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;

public class MainMenu extends Menu {

    public MainMenu(Connection connection) {
        super(connection);
    }

    public void run() throws SQLException {

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
                customer_registration();
            else if (choice.equals("3"))
                login_menu("owner");
            else if (choice.equals("4"))
                break;
            else
                System.out.println("\nInvalid input... please try again.\n");
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
            System.out.println("\nInvalid Credentials... returning to main menu.\n");
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
        if (username_taken(username)) {
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
        String[] shipping_address = address_registration("billing");
        String[] billing_address = address_registration("shipping");

        String shipping_postal_code = shipping_address[0];
        String shipping_street_address = shipping_address[1];
        String billing_postal_code = billing_address[0];
        String billing_street_address = billing_address[1];

        // Insert into user and customer tables
        QueryUtilityFunctions.insert_into_table(
                connection, "user_account", new String[]{username, password, first_name, last_name, email});

        QueryUtilityFunctions.insert_into_table(
                connection, "customer", new String[]{username, credit_card_number, billing_postal_code,
                billing_street_address, shipping_postal_code, shipping_street_address});
    }

    public String[] address_registration(String addressType) throws SQLException {

        System.out.println(addressType + " Information");
        System.out.println("-------------------");
        System.out.print("Street address: ");
        String street_address = scan.nextLine().toLowerCase();
        System.out.print("Postal code: ");
        String postal_code = scan.nextLine().toLowerCase();

        // Check if billing postal code already in database. If not, ask more questions and insert into area table
        if (!postal_code_in_database(postal_code)) {
            System.out.print("City: ");
            String city = scan.nextLine().toLowerCase();
            System.out.print("Province/state: ");
            String province = scan.nextLine().toLowerCase();
            System.out.print("Country: ");
            String country = scan.nextLine().toLowerCase();
            QueryUtilityFunctions.insert_into_table
                    (connection, "area", new String[]{postal_code, city, province, country});
        }
        // Insert into address table, unless tuple already exists
        if (!QueryUtilityFunctions.attributes_in_table(
                connection, "address", new String[]{"postal_code", "street_address"}, new String[]{postal_code, street_address}))
            QueryUtilityFunctions.insert_into_table(connection, "address", new String[]{postal_code, street_address});

        return new String[]{postal_code, street_address};
    }

    public boolean username_taken(String username) throws SQLException {
        return QueryUtilityFunctions.attribute_in_relation(connection,"user_account", "username", username);
    }

    public boolean postal_code_in_database(String postal_code) throws SQLException {
        return QueryUtilityFunctions.attribute_in_relation(connection,"area", "postal_code", postal_code);
    }

}
