package ru.umeta.libraryintegration.model

import javax.persistence.*
import java.util.Date

/**
 * Created by ctash on 30.04.2015.
 */
@Entity
@Table(name = "enriched_document")
class EnrichedDocument {

    @Id
    @Column(name = "id")
    @GeneratedValue
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "title_string_id")
    var title: StringHash? = null

    @ManyToOne
    @JoinColumn(name = "author_string_id")
    var author: StringHash? = null

    @Column(name = "isbn")
    var isbn: String? = null

    @Column(name = "xml", columnDefinition = "BLOB")
    var xml: String? = null

    @Column(name = "creation_time")
    var creationTime: Date? = null

    @Column(name = "publish_year")
    var publishYear: Int? = null

    @OneToMany(mappedBy = "enrichedDocument")
    var documents: Collection<Document>? = null
}

