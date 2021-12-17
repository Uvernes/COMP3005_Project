import java.sql.Connection;
import java.sql.SQLException;

public class CustomerMenu extends UserMenu {

    public CustomerMenu(Connection connection, String username) { super(connection, username); }

    public void run() throws SQLException {
        System.out.println("\nGreetings, " + username + "!");

        while (true) {
            System.out.print(
                    "\nOptions\n"
                            + "-------\n"
                            + "1. Browse books\n"
                            + "2. View Cart\n"
                            + "3. View Orders\n"
                            + "4. Logout\n\n"
                            + "Please select an option (type in the corresponding number): ");

            String choice = scan.nextLine();

            if (choice.equals("1"))
                browse_books();
            else if (choice.equals("2"))
                cart_menu();
            else if (choice.equals("3"))
                orders_menu();
            else if (choice.equals("4"))
                break;
            else
                System.out.println("\nInvalid input... please try again.\n");
        }
        System.out.println("\nLogging out...\n");
    }

    // 1st function
    // Browse using prefixes to receive slight bonus mark
    // To browse books, we simply need to traverse books based on a search condition.
    // Then we acquire a result, which is just a list of books. Then we display each book, and give the option to see 1 in more detail.
    // To see more about a specific book, display_book() is called, And to display books,
    public void browse_books() {

    }

    // 2nd function
    public void cart_menu() {

    }

    // 3rd function
    // Similar idea to books. May want to create an Order class.
    public void orders_menu() {

    }

}
