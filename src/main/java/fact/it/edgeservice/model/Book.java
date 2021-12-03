package fact.it.edgeservice.model;

public class Book {
    private int id;
    private String title;
    private String ISBN;
    private String category;

    public Book() {
    }

    public Book(int id, String title, String ISBN, String category) {
        this.id = id;
        this.title = title;
        this.ISBN = ISBN;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}