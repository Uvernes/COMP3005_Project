public class Book {

    String ISBN, title, genre;
    int page_count, publication_year, stock;
    float price;

    Book(String ISBN, String title, String genre, int page_count, int publication_year, int stock, float price) {
        this.ISBN = ISBN;
        this.title = title;
        this.genre = genre;
        this.page_count = page_count;
        this.publication_year = publication_year;
        this.stock = stock;
        this.price = price;
    }

}
