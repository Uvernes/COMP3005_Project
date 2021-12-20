import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class OwnerMenu extends UserMenu {

    private ArrayList<Order> orders;
    private int monthly_expenditures = 400;
    private int horror_total;
    private int comedy_total;
    private int phil_total;
    private int total_sales;

    public OwnerMenu(Connection connection, String username) throws SQLException { super(connection, username); }

    public void run() throws SQLException {
        System.out.println("\nGreetings, " + getUsername() + "!");

        while(true){
            System.out.print(
                    "\nOptions\n"
                            + "-------\n"
                            + "1. Search books\n"
                            + "2. Add book\n"
                            + "3. View reports\n"
                            + "4. Logout\n\n"
                            + "Please select an option (type in the corresponding number): "
            );

            String choice = scan.nextLine();

            if (choice.equals("1"))
                browse_books_menu();
            else if (choice.equals("2"))
                add_book();
            else if(choice.equals("3"))
                print_reports();
            else if(choice.equals("4"))
                break;
        }
        System.out.println("\nLogging out...\n");
    }

    /*
    The bookstore owners can add new books
    to their collections, or remove books from their store. They also need to store information on the publishers of
    books such as name, address, email address, phone number(s), banking account, etc.. The banking account for
    publishers is used to transfer a percentage of the sales of books published by these publishers. This percentage
    is variable and changes from one book to another. The owners should have access to reports that show sales
    vs. expenditures, sales per genres, sales per author, etc..
    */
    // Overrides abstract method
    public void individual_book_menu(ArrayList<Book> searched_books, Book book) throws SQLException{
        while(true){

            scan = new Scanner(System.in);

            System.out.println(book);
            System.out.println("Options:");
            System.out.println("1. Remove book");
            System.out.println("2. Go back");
            System.out.print("\nPlease select an option (type in the corresponding number): ");
            String choice = scan.nextLine();

            if(choice.equals("1")){

                System.out.println("\nRemoved book from store.\n");
                remove_book(book);
                for(Book b: searched_books){
                    if(b.getISBN().equals(book.getISBN())){
                        searched_books.remove(b);
                        break;
                    }
                }
                return;
            }
            else if (choice.equals("2"))
                return;
            else
                System.out.println("\nInvalid input... please try again.\n");
        }
    }

    public void remove_book(Book book) throws SQLException{
        //delete book from db
        //delete from writes where isbn = book.isbn
        String query2 = "delete from writes where ISBN = '" + book.getISBN() + "'";
        connection.prepareStatement(query2).execute();

        String query3 = "delete from publishes where ISBN = '" + book.getISBN() + "'";
        connection.prepareStatement(query3).execute();

        String query4 = "delete from book_order where ISBN = '" + book.getISBN() + "'";
        connection.prepareStatement(query4).execute();

        String query = "delete from book where ISBN = '" + book.getISBN() + "'";
        connection.prepareStatement(query).execute();

        for(Book b: getBooks()){
            if(b.getISBN().equals(book.getISBN())){
                getBooks().remove(b);
                break;
            }
        }
    }

    public void add_book() throws SQLException{

        System.out.print("\nAdding a new book\n");
        System.out.print("------------------\n");
        System.out.print("Enter the ISBN number: ");
        String isbn = scan.nextLine();

        System.out.print("Enter the Title: ");
        String title = scan.nextLine();

        System.out.print("Enter the author's first name: ");
        String first = scan.nextLine();
        System.out.print("Enter the author's last name: ");
        String last = scan.nextLine();

        System.out.print("Enter the Genre: ");
        String genre = scan.nextLine();

        System.out.print("Enter the Publication Year: ");
        int pub_yearS = Integer.parseInt(scan.nextLine());

        System.out.print("Enter the Page Count: ");
        int page_count = Integer.parseInt(scan.nextLine());

        System.out.print("Enter the Price: ");
        float price = Float.parseFloat(scan.nextLine());

        System.out.print("Enter the Available Stock: ");
        int stock = Integer.parseInt(scan.nextLine());

        System.out.print("Enter the Publisher's Sales Percentage of this book as a decimal: ");
        String percentage = String.format("%.2f", Float.parseFloat(scan.nextLine()));

        QueryUtilityFunctions.insert_into_table(connection, "book", new String[]{"string","string","string","int","int","float","int"}, new String[]{isbn, title, genre, Integer.toString(pub_yearS), Integer.toString(page_count), Float.toString(price), Integer.toString(stock)} );

        QueryUtilityFunctions.insert_into_table(connection, "publishes", new String[]{"string", "string", "float"}, new String[]{"Cheesecake Books", isbn, percentage});

        String query = "insert into author values(default,?,?)";
        // Note: must get access to the newly added tuple in order to see what the author id is
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1,first);
        preparedStatement.setString(2,last);
        preparedStatement.executeUpdate();
        ResultSet rset = preparedStatement.getGeneratedKeys();
        rset.next();
        int author_id = rset.getInt(1);

        QueryUtilityFunctions.insert_into_table(connection, "writes", new String[]{"int", "string"}, new String[]{Integer.toString(author_id) , isbn});

        this.addBook(new Book(isbn, title, genre, pub_yearS,page_count, price, stock));
        // Reload books
        load_books();
    }

    // Did not have time to finish. But did create an Order class, which constitutes some of the work.
    // For some reports, simply need to load in the Order objects and generate a report, traversing order objects
    // and their attributes
    public void print_reports() throws SQLException {

        ResultSet orders_set = QueryUtilityFunctions.get_table(connection, "orders");
        System.out.println("\nComing soon...\n");
//        while (orders_set.next()) {
//            orders.add(new Order(
//                   orders_set.getInt(1), orders_set.getString(2), orders_set.getString(3), orders_set.getString(4), orders_set.getString(5), orders_set.getString(6), orders_set.getString(7), orders_set.getString(8), orders_set.getString(9)));
//        }
    }

}