package ru.umeta.libraryintegration.model;

import javax.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "enriched_id")
    private EnrichedDocument enrichedDocument;

    public EnrichedDocument() {
    }
}

