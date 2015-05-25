package ru.umeta.libraryintegration.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by ctash on 29.04.2015.
 */
@Entity
@Table(name = "protocol")
public class Protocol implements Serializable{

    private static final long serialVersionUID = 4908088548280013641L;

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @NaturalId(mutable = true)
    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "protocol")
    private Collection<Document> document;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Document> getDocument() {
        return document;
    }

    public void setDocument(Collection<Document> document) {
        this.document = document;
    }

    public Protocol() {
    }
}
