package ru.umeta.libraryintegration.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by ctash on 29.04.2015.
 */

public class StringHash {

    private Long id;

    private String value;

    private Byte hashPart1;

    private Byte hashPart2;

    private Byte hashPart3;

    private Byte hashPart4;

    private Collection<Document> isTitleOfDocuments;

    private Collection<Document> isAuthorOfDocuments;

    private Collection<EnrichedDocument> isTitleOfEnrichedDocuments;

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
