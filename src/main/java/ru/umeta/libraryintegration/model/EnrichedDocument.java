package ru.umeta.libraryintegration.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

/**
 * Created by ctash on 30.04.2015.
 */
public class EnrichedDocument {

    private Long id;

    private StringHash title;

    private StringHash author;

    private String isbn;

    private String xml;

    private Date creationTime;

    private Integer publishYear;

    private Collection<Document> documents;

    public EnrichedDocument() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StringHash getTitle() {
        return title;
    }

    public void setTitle(StringHash title) {
        this.title = title;
    }

    public StringHash getAuthor() {
        return author;
    }

    public void setAuthor(StringHash author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Collection<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Collection<Document> documents) {
        this.documents = documents;
    }

    public Integer getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(Integer publishYear) {
        this.publishYear = publishYear;
    }
}

