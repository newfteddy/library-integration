package ru.umeta.libraryintegration.model

/**
 * Created by k.kosolapov on 11/26/2015.
 */
data class EnrichedDocumentLite(
        val id: Long,
        val authorId: Long,
        val titleId: Long,
        val isbn: Int,
        val publishYear: Int)
