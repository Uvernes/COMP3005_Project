/*
COMP 3005 - Project Implementation
By: Majd Taweel (101159849), Uvernes Somarriba (101146733)
 */

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
