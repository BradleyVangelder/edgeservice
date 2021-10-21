package fact.it.edgeservice.model;

public class Quote {
    private int id;
    private String quote;
    private String ISBN;

    public Quote() {}

    public Quote(String quote, String ISBN) {
        setISBN(ISBN);
        setQuote(quote);
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }
}
