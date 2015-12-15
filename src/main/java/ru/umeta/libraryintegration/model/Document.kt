package ru.umeta.libraryintegration.model

import org.hibernate.type.TextType

import javax.persistence.*
import java.util.Date

/**
 * Created by k.kosolapov on 27.04.2015.
 */
@Entity
@Table(name = "document")
class Document {

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

    @ManyToOne
    @JoinColumn(name = "protocol_id")
    var protocol: Protocol? = null

    @ManyToOne
    @JoinColumn(name = "enriched_id")
    var enrichedDocument: EnrichedDocument? = null

    @Column(name = "distance")
    var distance: Double? = null
}
