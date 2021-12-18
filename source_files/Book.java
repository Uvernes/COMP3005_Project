import java.util.ArrayList;

public class Book {

    private String ISBN, title, genre;
    private ArrayList<String> authors;
    private int page_count, publication_year, stock;
    private float price;

    Book(String ISBN, String title, String genre, int publication_year, int page_count, float price, int stock) {
        this.ISBN = ISBN;
        this.title = title;
        this.genre = genre;
        this.page_count = page_count;
        this.publication_year = publication_year;
        this.stock = stock;
        this.price = price;
        authors = new ArrayList<String>();
    }

    public String toString() {
        String dashed_lines = "";
        for (int i = 0; i < ("Book - " + title).length(); i++)
            dashed_lines += "-";

        String authors_line = "";
        for (String author: authors) {
            authors_line += author + ", ";
        }
        authors_line = authors_line.substring(0, authors_line.length() - 2);

        return "\nBook - " + title + "\n" +
                dashed_lines + "\n" +
                "ISBN: " + ISBN + "\n" +
                "Genre: " + getGenre() + '\n' +
                "Author(s): " + authors_line + "\n" +
                "Page count: " + page_count + "\n" +
                "Publication year: " + publication_year + "\n" +
                "Stock: " + stock + "\n" +
                "Price: $" + String.format("%.2f", price) + "\n";
    }

    public void add_author(String author) { authors.add(author); }

    public String getISBN() { return ISBN; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public ArrayList<String> getAuthors() { return authors; }
    public int getPage_count() { return page_count; }
    public int getPublication_year() { return publication_year; }
    public int getStock() { return stock; }
    public float getPrice() { return price; }

    public void setStock(int stock) {this.stock = stock; }

}
