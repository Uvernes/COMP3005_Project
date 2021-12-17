import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class OwnerMenu extends UserMenu {

    public OwnerMenu(Connection connection, String username) { super(connection, username); }

    public void run() throws SQLException {
        System.out.println("\nGreetings, " + username + "!");
    }
}
