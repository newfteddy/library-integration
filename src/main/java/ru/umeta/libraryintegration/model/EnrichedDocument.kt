package ru.umeta.libraryintegration.model

import java.util.*
import javax.persistence.*

/**
 * Created by ctash on 30.04.2015.
 */
@Entity
@Table(name = "enriched_document")
data class EnrichedDocument(
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
        val publishYear: Int? = null)

