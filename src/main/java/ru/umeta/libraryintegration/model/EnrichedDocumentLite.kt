package ru.umeta.libraryintegration.model

import java.util.HashSet

/**
 * Created by k.kosolapov on 11/26/2015.
 */
class EnrichedDocumentLite {

    var id: Long = 0
    var titleTokens: Set<String>
    var authorTokens: Set<String>
    var nullIsbn = true

    fun isbnIsNull(): Boolean {
        return nullIsbn
    }

}
