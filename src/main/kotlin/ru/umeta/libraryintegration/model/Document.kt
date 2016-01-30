package ru.umeta.libraryintegration.model

import java.util.*

/**
 * Created by k.kosolapov on 27.04.2015.
 */
data class Document(
        var id: Long,

        val title: StringHash,

        val author: StringHash,

        val isbn: String? = null,

        val xml: String? = null,

        val creationTime: Date,

        val publishYear: Int? = null,

        var enrichedDocument: EnrichedDocumentLite?)
