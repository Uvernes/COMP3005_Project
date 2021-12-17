import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

abstract class Menu {

    Scanner scan;
    Connection connection;

    Menu(Connection connection) {
        this.connection = connection;
        scan = new Scanner(System.in);
    }

    public abstract void run() throws SQLException;
}
