package ru.umeta.libraryintegration.model;

import org.hibernate.type.TextType;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by k.kosolapov on 27.04.2015.
 */
public class Document {

    private Long id;

    private StringHash title;

    private StringHash author;
    private String isbn;

    private String xml;

    private Date creationTime;

    private Integer publishYear;

    private Protocol protocol;

    private EnrichedDocument enrichedDocument;

    private Double distance;

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

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public EnrichedDocument getEnrichedDocument() {
        return enrichedDocument;
    }

    public void setEnrichedDocument(EnrichedDocument enrichedDocument) {
        this.enrichedDocument = enrichedDocument;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Document() {
    }

    public Integer getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(Integer publishYear) {
        this.publishYear = publishYear;
    }
}
