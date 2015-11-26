package ru.umeta.libraryintegration.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by k.kosolapov on 11/26/2015.
 */
public class EnrichedDocumentLite {

    public long id;
    public Set<String> titleTokens;
    public Set<String> authorTokens;
    public boolean nullIsbn = true;

    public EnrichedDocumentLite() {
    }

    public boolean isbnIsNull() {
        return nullIsbn;
    }

}
