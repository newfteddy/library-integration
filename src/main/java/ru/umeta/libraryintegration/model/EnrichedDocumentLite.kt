package ru.umeta.libraryintegration.model

import gnu.trove.set.hash.TIntHashSet

/**
 * Created by k.kosolapov on 11/26/2015.
 */
data class EnrichedDocumentLite(
        val id: Long,
        val authorId: Long,
        val titleId: Long,
        var nullIsbn: Boolean = true) {
    fun isbnIsNull(): Boolean {
        return nullIsbn;
    }
}
