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
        connection.prepareStatement(query).executeUpdate();
    }

    public void complete_order() throws SQLException {

        if (books.size() == 0) {
            System.out.println("\nCart is empty. Please add to it before you complete your order.");
            return;
        }

        float total = 0;
        for (int i = 0; i < books.size(); i++) {
            total += books.get(i).getPrice() * quantities.get(i);
        }
        System.out.println("\nYour total is: $" + String.format("%.2f", total));
        System.out.println("Payment will be done with your registration credit card.");
        System.out.print("\nTo confirm, type 'yes': ");

        String input = scan.nextLine();

        if (!input.toLowerCase().equals("yes")) {
            System.out.println("Confirmation failed. Returning to cart menu.");
            return;
        }

        // Ask for shipping, billing addresses (or if they want to use the one from registration).
        // Like before, if you need to add to area, address relations, do so
        String[] shipping_address = order_address_setup("shipping");
        String[] billing_address = order_address_setup("billing");
        String shipping_postal_code = shipping_address[0];
        String shipping_street_address = shipping_address[1];
        String billing_postal_code = billing_address[0];
        String billing_street_address = billing_address[1];
        // NOTE: ** nothing done with billing address since not in order table. 'Billing' is done through email

        // All orders begin by being shipped out from location defined below (where owner uvito5 lives - a warehouse!)
        String current_postal_code = "T2A 1C8";
        String current_street_address = "3536 40th Street";

        // Add to order table
        String query = "insert into order_info values(default,?,?,?,?,?,?,?,?)";
        // Note: must get access to the newly added tuple in order to see what it's order number is
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1,username);
        preparedStatement.setString(2,shipping_postal_code);
        preparedStatement.setString(3,shipping_street_address);
        preparedStatement.setString(4,current_postal_code);
        preparedStatement.setString(5,current_street_address);
        preparedStatement.setString(6,"processing");
        preparedStatement.setString(7,"2021/12/18");
        preparedStatement.setString(8,"Willy's Warehouse");
        preparedStatement.executeUpdate();
        ResultSet rset = preparedStatement.getGeneratedKeys();
        rset.next();
        int order_number = rset.getInt(1);

        // Add to book order table
        for (int i = 0; i < books.size(); i++) {
            QueryUtilityFunctions.insert_into_table(connection, "book_order", new String[]{"int", "string", "int"},
                    new String[]{Integer.toString(order_number), books.get(i).getISBN(), Integer.toString(quantities.get(i))});
        }

        // Remove all user's basket_items from basket_item table
        query = "delete from basket_item where username = '" + username + "'";
        connection.prepareStatement(query).executeUpdate();

        books.clear();
        quantities.clear();

        System.out.println("\norder complete!");

    }

    private String[] order_address_setup(String addressType) throws SQLException {
        Scanner scan = new Scanner(System.in);
        String input;
        String query;
        String[] address = new String[2];

        while (true) {
            System.out.print("\nUse same " + addressType + " address as in registration (type 'yes' or 'no'): ");
            input = scan.nextLine();
            // Get customer's billing postal code and street address
            if (input.toLowerCase().equals("yes")) {
                query = "select " + addressType + "_postal_code, " + addressType +
                        "_street_address from customer where username='" + username + "'";
                ResultSet rset = connection.prepareStatement(query).executeQuery();
                rset.next();
                address[0] = rset.getString(addressType + "_postal_code");
                address[1] = rset.getString(addressType + "_street_address");
                break;
            }
            else if (input.toLowerCase().equals("no")) {
                System.out.println();
                address = QueryUtilityFunctions.address_registration(connection, addressType);
                break;
            }
            else
                System.out.println("\nInvalid input... please try again.");
        }
        return address;
    }

    public ArrayList<Book> getBooks() { return books; }
    public ArrayList<Integer> getQuantities() { return quantities; }

}
