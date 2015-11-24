package ru.umeta.libraryintegration.model;

import javax.persistence.*;

/**
 * Created by Kirill Kosolapov (https://github.com/c-tash) on 23.11.2015.
 */
@Entity
@Table(name = "enriched_document")
public class EnrichedXmlBlob {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @Column(name = "xml", columnDefinition = "TEXT")
    private String xml;

}
