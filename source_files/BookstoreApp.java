import javax.swing.plaf.nimbus.State;
import java.util.Scanner;
import java.sql.*;

public class BookstoreApp {
    public static void main(String[] args) {

        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookstore_database", "postgres", "student");
            (new MainMenu(connection)).run();
        }
        catch (Exception sqle) {
            System.out.println("SQL Exception: " + sqle);
        }
    }
}
