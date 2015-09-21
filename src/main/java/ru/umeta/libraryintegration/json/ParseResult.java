package ru.umeta.libraryintegration.json;

/**
 * Created by k.kosolapov on 06.04.2015.
 */
public abstract class ParseResult {
    protected String title;
    protected String isbn;
    protected String author;

    protected ParseResult(String title, String isbn, String author) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getAuthor() {
        return author;
    }

    abstract public ParseResult clone();
}
