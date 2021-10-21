package fact.it.edgeservice.model;

public class Book {
    private int id;
    private String title;
    private String ISBN;

    public Book() {
    }

    public Book(String title, String ISBN) {
        this.title = title;
        this.ISBN = ISBN;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }
}