package ru.umeta.libraryintegration.model

import java.util.*
import javax.persistence.*

/**
 * Created by k.kosolapov on 27.04.2015.
 */
@Entity
@Table(name = "document")
data class Document(
        @Id
        @Column(name = "id")
        @GeneratedValue
        var id: Long,

        @ManyToOne
        @JoinColumn(name = "title_string_id")
        val title: StringHash,

        @ManyToOne
        @JoinColumn(name = "author_string_id")
        val author: StringHash,

        @Column(name = "isbn")
        val isbn: String? = null,

        @Column(name = "xml", columnDefinition = "BLOB")
        val xml: String? = null,

        @Column(name = "creation_time")
        val creationTime: Date,

        @Column(name = "publish_year")
        val publishYear: Int? = null,

        @Column(name = "enriched_id")
        var enrichedDocument: EnrichedDocumentLite?)
