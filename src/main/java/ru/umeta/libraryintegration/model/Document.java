package ru.umeta.libraryintegration.model;

import org.hibernate.type.TextType;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by k.kosolapov on 27.04.2015.
 */
@Entity
@Table(name = "document")
public class Document {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "title_string_id")
    private StringHash title;

    @ManyToOne
    @JoinColumn(name = "author_string_id")
    private StringHash author;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "xml", columnDefinition = "TEXT")
    private String xml;

    @Column(name = "creation_time")
    private Date creationTime;

    @ManyToOne
    @JoinColumn(name = "protocol_id")
    private Protocol protocol;

    @ManyToOne
    @JoinColumn(name = "enriched_id")
    private EnrichedDocument enrichedDocument;

    @Column(name = "distance")
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
}
