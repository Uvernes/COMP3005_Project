import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class OwnerMenu extends UserMenu {

    public OwnerMenu(Connection connection, String username) throws SQLException { super(connection, username); }

    public void run() throws SQLException {
        System.out.println("\nGreetings, " + getUsername() + "!");
    }

    // Overrides abstract method
    public void individual_book_menu(Book book) {
        System.out.println(book);
        // ...
    }
}
