package fact.it.edgeservice.model;

import java.util.List;

public class BookQuote {
    private String bookTitle;
    private String ISBN;
    private Quote quote;

    public BookQuote(String bookTitle, String ISBN, Quote quote) {
        this.bookTitle = bookTitle;
        this.ISBN = ISBN;
        this.quote = quote;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }
}
