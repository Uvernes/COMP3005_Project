import java.security.interfaces.RSAKey;
import java.util.ArrayList;
import java.sql.*;
import java.util.Scanner;

public class Cart {

    // These parallel array lists define what the contents of the cart are
    private ArrayList<Book> books;
    private ArrayList<Integer> quantities;

    private String username;
    private Connection connection;
    private Scanner scan;

    Cart(Connection connection, ArrayList<Book> all_books, String username) throws SQLException {
        this.connection = connection;
        this.scan = new Scanner(System.in);
        this.username = username;
        books = new ArrayList<>();
        quantities = new ArrayList<>();
        String query = "select * from book natural join basket_item where username = '" + username + "'";
        ResultSet rset = connection.prepareStatement(query).executeQuery();

        while (rset.next()) {
            // Find book corresponding to basket item
            for (Book book : all_books) {
                if (book.getISBN().equals(rset.getString("ISBN"))) {
                    books.add(book);
                    break;
                }
            }
            quantities.add(rset.getInt("quantity"));
        }
    }

    public void clear() throws SQLException {
        for (int i = books.size() - 1; i>= 0; i--) {
            books.get(i).setStock(books.get(i).getStock() + quantities.get(i));
            // Update stock for book
            String query = "update book set stock = " + Integer.toString(books.get(i).getStock()) +
                    " where ISBN = '" + books.get(i).getISBN() + "'";
            connection.prepareStatement(query).execute();
            books.remove(i);
            quantities.remove(i);
        }
        // Remove all user's basket_item tuples
        String query = "delete from basket_item where username = '" + username + "'";
        connection.prepareStatement(query).execute();
    }

    public void complete_order() {
        float total = 0;
        System.out.println("\nTotal:");
        for (int i = 0; i < books.size(); i++) {
            total += books.get(i).getPrice() * quantities.get(i);
        }
        System.out.println("\nYour total is: $" + String.format("%.2f, total"));
        System.out.println("Payment will be done with your registration credit card.");
        System.out.print("\nTo confirm, type 'yes': ");

        String input = scan.nextLine();

        if (!input.toLowerCase().equals("yes")) {
            System.out.println("Confirmation failed. Returning to cart menu.");
        }

        // Ask for shipping, billing addresses (or if they want to use the one from registration).
        // Like before, if you need to add to area, address relations, do so

        // Add to order table

        // Add to book order table

        // Remove all user's basket_items from basket_item table


        System.out.println("\norder complete!");


    }

    public ArrayList<Book> getBooks() { return books; }
    public ArrayList<Integer> getQuantities() { return quantities; }

}
