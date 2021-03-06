import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CustomerMenu extends UserMenu {

    public CustomerMenu(Connection connection, String username) throws SQLException { super(connection, username); }

    public void run() throws SQLException {
        System.out.println("\nGreetings, " + getUsername() + "!");

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
                browse_books_menu();
            else if (choice.equals("2"))
                cart_menu();
            else if (choice.equals("3"))
                orders_menu();
            else if (choice.equals("4"))
                break;
            else
                System.out.println("\nInvalid input... please try again.");
        }
        System.out.println("\nLogging out...\n");
    }

    // 1st function
    public void individual_book_menu(ArrayList<Book> searched_books, Book book) throws SQLException {

        // Give option to add book to cart (i.e basket_item table)

        while (true) {

            // Reinitialize scanner each time to avoid bugs
            scan = new Scanner(System.in);

            System.out.println(book);
            System.out.println("Options:");
            System.out.println("1. Add book to cart");
            System.out.println("2. Go back");
            System.out.print("\nPlease select an option (type in the corresponding number): ");
            String choice = scan.nextLine();

            if (choice.equals("1")) {

                System.out.print("\nQuantity: ");
                int quantity;
                try {
                    quantity = scan.nextInt();
                    add_to_cart(book, quantity);
                }
                catch (InputMismatchException e) {
                    System.out.println("\nYou didn't enter a number.");
                }
            }
            else if (choice.equals("2"))
                return;
            else
                System.out.println("\nInvalid input... please try again.\n");
        }
    }

    public void add_to_cart(Book book, int quantity) throws SQLException {
        if (quantity < 1) {
            System.out.println("\nQuantity requested cannot be less than 1.");
            return;
        }
        if (book.getStock() < quantity) {
            System.out.println("\nThe quantity you requested exceeds the available stock.");
            return;
        }
        // Update book quantity
        int new_stock = book.getStock() - quantity;
        // Analogous to a trigger if the stock gets too low
        if (new_stock < 10)
            new_stock = 20;
        String query = "update book set stock = " + Integer.toString(new_stock) + " where ISBN = '" + book.getISBN() + "'";
        connection.prepareStatement(query).executeUpdate();
        book.setStock(new_stock);

        // NOTE: When book added to cart, stock goes down. If deleted from cart, stock goes up
        // This means items in a person's cart are reserved - LOL

        // Check if book already in cart. If so, simply add to the quantity variable.
        boolean book_in_cart = false;
        ResultSet rset = QueryUtilityFunctions.get_table(connection, "basket_item");
        while (rset.next()) {
            if (rset.getString("ISBN").equals(book.getISBN()) &&
                    rset.getString("username").equals(getUsername())) {
                // Update quantity
                book_in_cart = true;
                query = "update basket_item set quantity = " +
                        Integer.toString(rset.getInt("quantity") + quantity) + "where " +
                        "ISBN = '" + book.getISBN() + "' and username = '" + getUsername() + "'";
                connection.prepareStatement(query).executeUpdate();
            }
        }
        // If book not in cart, add it
        if (!book_in_cart) {
            QueryUtilityFunctions.insert_into_table(connection, "basket_item",
                    new String[]{"string", "string", "int"}, new String[]{book.getISBN(), getUsername(), Integer.toString(quantity)});
        }

        System.out.println("\nAdded to cart.");
    }

    public void cart_menu() throws SQLException {

        Cart cart = new Cart(connection, getBooks(), getUsername());

        while (true) {

            System.out.println("\nCart");
            System.out.println("----");

            for (int i = 0; i < cart.getBooks().size(); i++) {
                System.out.println("- " + cart.getBooks().get(i).getTitle() + " (" +
                        "quantity: " + Integer.toString(cart.getQuantities().get(i)) + ")");
            }

            System.out.println("\nOptions");
            System.out.println("1. Clear cart");
            System.out.println("2. Complete purchase");
            System.out.println("3. Go back");

            System.out.print("\nPlease select an option (type in the corresponding number): ");
            String choice = scan.nextLine();

            if (choice.equals("1")) {
                cart.clear();
                System.out.println("\nCart successfully cleared.");
            }
            else if (choice.equals("2")) {
              cart.complete_order();
            }
            else if (choice.equals("3")) {
               return;
            }
            else
                System.out.println("\nInvalid input... please try again.");
        }
    }

    // 3rd function
    // Similar idea to books. May want to create an Order class.
    /*
    orders
    -------
    1. Order #(order id) |  Date:  | Status:(order_status)
    ...

    Options:
    Select number from above to view in detail. Type 'q' to go back.
     */
    public void orders_menu() throws SQLException {

        // first get all of the customers orders
        ArrayList<Order> orders = new ArrayList<>();
        String query = "select * from order_info where customer = '" + getUsername() + "'";
        ResultSet rset = connection.prepareStatement(query).executeQuery();
        while (rset.next()) {
            orders.add(new Order(rset.getInt(1), rset.getString(2), rset.getString(3),
                    rset.getString(4), rset.getString(5), rset.getString(6),
                    rset.getString(7), rset.getString(8), rset.getString(9)));
        }

        while (true) {
            System.out.println("\nOrders");
            System.out.println("------");
            for (int i = 0; i < orders.size(); i++) {
                System.out.println(Integer.toString(i + 1) + ". Order #" + orders.get(i).getOrderNumber() +
                        " (Status: " + orders.get(i).getOrderStatus() + ")");
            }
            System.out.println("\nTo view an order in more detail, type in the corresponding number. To go back, type 'q'.");
            System.out.print("\nYour selection: ");
            String choice = scan.nextLine();
            if (choice.equals("q"))
                break;
            boolean valid_choice = false;
            for (int i = 0; i < orders.size(); i++)
                if (choice.equals(Integer.toString(i+1))) {
                    System.out.println();
                    System.out.println(orders.get(i));
                    valid_choice = true;
                    break;
                }
            if (!valid_choice)
                System.out.println("\nInvalid input... please try again.\n");
        }

    }

}
