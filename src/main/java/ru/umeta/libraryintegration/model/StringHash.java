package ru.umeta.libraryintegration.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by ctash on 29.04.2015.
 */
@Entity
@Table(name = "string_hash",
        indexes = {
                @Index(name = "IDX_HASH_12",
                        columnList = "hash_part_1, hash_part_2"
                ),
                @Index(name = "IDX_HASH_13",
                        columnList = "hash_part_1, hash_part_3"
                ),
                @Index(name = "IDX_HASH_14",
                        columnList = "hash_part_1, hash_part_4"
                ),
                @Index(name = "IDX_HASH_23",
                        columnList = "hash_part_2, hash_part_3"
                ),
                @Index(name = "IDX_HASH_24",
                        columnList = "hash_part_2, hash_part_4"
                ),
                @Index(name = "IDX_HASH_34",
                        columnList = "hash_part_3, hash_part_4"
                ),


        })
public class StringHash {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @NaturalId(mutable = false)
    @Column(name = "value", nullable = false, unique = true)
    private String value;

    @Column(name = "hash_part_1", nullable = false)
    private Byte hashPart1;

    @Column(name = "hash_part_2", nullable = false)
    private Byte hashPart2;

    @Column(name = "hash_part_3", nullable = false)
    private Byte hashPart3;

    @Column(name = "hash_part_4", nullable = false)
    private Byte hashPart4;

    @OneToMany(mappedBy = "title")
    private Collection<Document> isTitleOfDocuments;

    @OneToMany(mappedBy = "author")
    private Collection<Document> isAuthorOfDocuments;

    @OneToMany(mappedBy = "title")
    private Collection<EnrichedDocument> isTitleOfEnrichedDocuments;

    @OneToMany(mappedBy = "author")
    private Collection<EnrichedDocument> isAuthorOfEnrichedDocuments;

    public StringHash() {
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

    public Byte getHashPart1() {
        return hashPart1;
    }

    public void setHashPart1(Byte cachePart1) {
        this.hashPart1 = cachePart1;
    }

    public Byte getHashPart2() {
        return hashPart2;
    }

    public void setHashPart2(Byte cachePart2) {
        this.hashPart2 = cachePart2;
    }

    public Byte getHashPart3() {
        return hashPart3;
    }

    public void setHashPart3(Byte cachePart3) {
        this.hashPart3 = cachePart3;
    }

    public Byte getHashPart4() {
        return hashPart4;
    }

    public void setHashPart4(Byte cachePart4) {
        this.hashPart4 = cachePart4;
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
