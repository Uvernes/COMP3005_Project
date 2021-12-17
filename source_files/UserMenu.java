import java.sql.Connection;
import java.sql.SQLException;

abstract class UserMenu extends Menu {

    String username;
    Book[] books;

    public UserMenu(Connection connection, String username) {
        super(connection);
        this.username = username;
        books = load_books();
    }

    // IMPLEMENT NEXT
    public Book[] load_books() {
        return null;
    }

    public void display_book(Book book) {

    }

    public abstract void run() throws SQLException;


}
