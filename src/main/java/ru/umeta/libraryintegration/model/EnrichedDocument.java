package ru.umeta.libraryintegration.model;

import javax.persistence.*;
import javax.persistence.metamodel.CollectionAttribute;
import java.util.Collection;
import java.util.Date;

/**
 * Created by ctash on 30.04.2015.
 */
@Entity
@Table(name = "EnrichedDocument")
public class EnrichedDocument {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "title_string_id")
    private StringCache title;

    @ManyToOne
    @JoinColumn(name = "author_string_id")
    private StringCache author;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "xml")
    private String xml;

    @Column(name = "creation_time")
    private Date creationTime;

    @OneToMany(mappedBy = "enrichedDocument")
    private Collection<Document> documents;

    public EnrichedDocument() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StringCache getTitle() {
        return title;
    }

    public void setTitle(StringCache title) {
        this.title = title;
    }

    public StringCache getAuthor() {
        return author;
    }

    public void setAuthor(StringCache author) {
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
}

