package fact.it.edgeservice.model;

import java.util.List;

public class BookQuotes {
    private String bookTitle;
    private String ISBN;
    private List<Quote> quotes;

    public BookQuotes(String bookTitle, String ISBN, List<Quote> quotes) {
        this.bookTitle = bookTitle;
        this.ISBN = ISBN;
        this.quotes = quotes;
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

    public List<Quote> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<Quote> quotes) {
        this.quotes = quotes;
    }
}
