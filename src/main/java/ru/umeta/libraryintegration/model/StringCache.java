package ru.umeta.libraryintegration.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by ctash on 29.04.2015.
 */
@Entity
@Table(name = "StringCache",
        indexes = {
                @Index(name = "IDX_CACHE_123",
                        columnList = "cache_part_1, cache_part_2, cache_part_3"
                ),
                @Index(name = "IDX_CACHE_234",
                        columnList = "cache_part_2, cache_part_3, cache_part_4"
                ),
                @Index(name = "IDX_CACHE_341",
                        columnList = "cache_part_3, cache_part_4, cache_part_1"
                ),
                @Index(name = "IDX_CACHE_412",
                        columnList = "cache_part_4, cache_part_1, cache_part_2"
                )

        })
public class StringCache {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "cache_part_1", nullable = false)
    private Byte cachePart1;

    @Column(name = "cache_part_2", nullable = false)
    private Byte cachePart2;

    @Column(name = "cache_part_3", nullable = false)
    private Byte cachePart3;

    @Column(name = "cache_part_4", nullable = false)
    private Byte cachePart4;

    @OneToMany(mappedBy = "title")
    private Collection<Document> isTitleOfDocuments;

    @OneToMany(mappedBy = "author")
    private Collection<Document> isAuthorOfDocuments;

    @OneToMany(mappedBy = "title")
    private Collection<EnrichedDocument> isTitleOfEnrichedDocuments;

    @OneToMany(mappedBy = "author")
    private Collection<EnrichedDocument> isAuthorOfEnrichedDocuments;

    public StringCache() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Byte getCachePart1() {
        return cachePart1;
    }

    public void setCachePart1(Byte cachePart1) {
        this.cachePart1 = cachePart1;
    }

    public Byte getCachePart2() {
        return cachePart2;
    }

    public void setCachePart2(Byte cachePart2) {
        this.cachePart2 = cachePart2;
    }

    public Byte getCachePart3() {
        return cachePart3;
    }

    public void setCachePart3(Byte cachePart3) {
        this.cachePart3 = cachePart3;
    }

    public Byte getCachePart4() {
        return cachePart4;
    }

    public void setCachePart4(Byte cachePart4) {
        this.cachePart4 = cachePart4;
    }

    public Collection<Document> getIsTitleOfDocuments() {
        return isTitleOfDocuments;
    }

    public void setIsTitleOfDocuments(Collection<Document> isTitleOfDocuments) {
        this.isTitleOfDocuments = isTitleOfDocuments;
    }

    public Collection<Document> getIsAuthorOfDocuments() {
        return isAuthorOfDocuments;
    }

    public void setIsAuthorOfDocuments(Collection<Document> isAuthorOfDocuments) {
        this.isAuthorOfDocuments = isAuthorOfDocuments;
    }

    public Collection<EnrichedDocument> getIsTitleOfEnrichedDocuments() {
        return isTitleOfEnrichedDocuments;
    }

    public void setIsTitleOfEnrichedDocuments(Collection<EnrichedDocument> isTitleOfEnrichedDocuments) {
        this.isTitleOfEnrichedDocuments = isTitleOfEnrichedDocuments;
    }

    public Collection<EnrichedDocument> getIsAuthorOfEnrichedDocuments() {
        return isAuthorOfEnrichedDocuments;
    }

    public void setIsAuthorOfEnrichedDocuments(Collection<EnrichedDocument> isAuthorOfEnrichedDocuments) {
        this.isAuthorOfEnrichedDocuments = isAuthorOfEnrichedDocuments;
    }
}
