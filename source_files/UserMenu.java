import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

abstract class UserMenu extends Menu {

    private String username;
    private ArrayList<Book> books;

    public UserMenu(Connection connection, String username) throws SQLException {
        super(connection);
        this.username = username;
        books = new ArrayList<Book>();
        load_books();
    }

    // IMPLEMENT NEXT
    public void load_books() throws SQLException {

        books.clear();
        ResultSet books_rset = QueryUtilityFunctions.get_table(connection, "book");
        while (books_rset.next()) {
            books.add(new Book(
                    books_rset.getString(1), books_rset.getString(2), books_rset.getString(3),
                    books_rset.getInt(4), books_rset.getInt(5), books_rset.getFloat(6),
                    books_rset.getInt(7)
            ));
        }

        // For each book, add author(s) name
        String query = "select * from author, writes where author.id = writes.author_id";
        ResultSet writes_rset = connection.prepareStatement(query).executeQuery();
        while (writes_rset.next()) {
            for (Book book : books) {
                if (book.getISBN().equals(writes_rset.getString("ISBN")))
                    book.add_author(writes_rset.getString("first_name") + " " + writes_rset.getString("last_name"));
            }
        }
    }

    public void browse_books_menu() throws SQLException {

        ArrayList<Book> books_result;

        while (true) {
            System.out.println("\nSearch options");
            System.out.println("--------------");
            System.out.println("1. By ISBN");
            System.out.println("2. By title");
            System.out.println("3. By genre");
            System.out.println("4. By publication year");
            System.out.println("5. Go back");
            System.out.print("\nPlease select an option (type in the corresponding number): ");
            String choice = scan.nextLine();

            if (choice.equals("1")) {
                System.out.print("\nEnter ISBN: ");
                books_result = search_books("ISBN", scan.nextLine());
            }
            else if (choice.equals("2")) {
                System.out.print("\nEnter title: ");
                books_result = search_books("title", scan.nextLine());
            }
            else if (choice.equals("3")) {
                System.out.print("\nEnter genre: ");
                books_result = search_books("genre", scan.nextLine());
            }
            else if (choice.equals("4")) {
                System.out.print("\nEnter publication year: ");
                books_result = search_books("publication year", scan.nextLine());
            }
            else if (choice.equals("5"))
                return;
            else {
                System.out.println("\nInvalid input... please try again.\n");
                continue;
            }
            display_books(books_result);
        }
    }

    // Note: When searching and substring are involved, we check for substrings. Case-insensitive
    public ArrayList<Book> search_books(String search_key, String value) {
        ArrayList<Book> books_result = new ArrayList<Book>();
        for (Book book : books) {
            if (
                    (search_key.equals("ISBN") && book.getISBN().toLowerCase().contains(value.toLowerCase())) ||
                            (search_key.equals("title") && book.getTitle().toLowerCase().contains(value.toLowerCase())) ||
                            (search_key.equals("genre") && book.getGenre().toLowerCase().contains(value.toLowerCase())) ||
                            (search_key.equals("publication_year") && Integer.toString(book.getPublication_year()).equals(value))
            )
                books_result.add(book);
        }
        return books_result;
    }

    public void display_books(ArrayList<Book> books) throws SQLException {

        System.out.println("\nSearch results");
        System.out.println("--------------");
        while (true) {
            for (int i = 0; i < books.size(); i++) {
                String cur_line = Integer.toString(i + 1) + ". " + books.get(i).getTitle() + " (";
                for (String author : books.get(i).getAuthors()) {
                    cur_line += author + ", ";
                }
                cur_line = cur_line.substring(0, cur_line.length() - 2) + ")";
                System.out.println(cur_line);
            }
            System.out.println("\nTo view a book in more detail, type in the corresponding number. To go back, type 'q'.");
            System.out.print("\nYour selection: ");
            String choice = scan.nextLine();
            if (choice.equals("q"))
                    break;
            boolean valid_choice = false;
            for (int i = 0; i < books.size(); i++)
                if (choice.equals(Integer.toString(i+1))) {
                    individual_book_menu(books, books.get(i));
                    valid_choice = true;
                    break;
            }
            if (!valid_choice)
                System.out.println("\nInvalid input... please try again.\n");
        }
    }

    public abstract void run() throws SQLException;
    public abstract void individual_book_menu(ArrayList<Book> searched_books, Book book) throws SQLException;

    public String getUsername() { return username; }
    public ArrayList<Book> getBooks() { return books; }
    public void addBook(Book b) {books.add(b); }
}
