package ru.umeta.libraryintegration.json;

/**
 * Created by k.kosolapov on 06.04.2015.
 */
public abstract class ParseResult {
    protected String title;
    protected String isbn;
    protected String author;
    protected Integer publishYear;

    protected ParseResult(String title, String isbn, String author, Integer year) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.publishYear = year;
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

    public void setPublishYear(Integer publishYear) {
        this.publishYear = publishYear;
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

    public Integer getPublishYear() {
        return publishYear;
    }


    abstract public ParseResult clone();
}
