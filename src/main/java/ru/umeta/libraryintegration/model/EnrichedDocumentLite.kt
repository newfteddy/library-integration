package ru.umeta.libraryintegration.model

import java.util.HashSet

/**
 * Created by k.kosolapov on 11/26/2015.
 */
class EnrichedDocumentLite(
        val id: Long,
        val authorTokens: Set<String>,
        val titleTokens: Set<String>,
        var nullIsbn: Boolean = true) {
    fun isbnIsNull(): Boolean {
        return nullIsbn;
    }
}
