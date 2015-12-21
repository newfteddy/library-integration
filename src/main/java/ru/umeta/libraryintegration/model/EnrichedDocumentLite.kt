package ru.umeta.libraryintegration.model

/**
 * Created by k.kosolapov on 11/26/2015.
 */
data class EnrichedDocumentLite(
        val id: Long,
        val authorTokens: Set<String>,
        val titleTokens: Set<String>,
        var nullIsbn: Boolean = true) {
    fun isbnIsNull(): Boolean {
        return nullIsbn;
    }
}
