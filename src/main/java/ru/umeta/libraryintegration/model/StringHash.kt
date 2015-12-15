package ru.umeta.libraryintegration.model

import org.hibernate.annotations.NaturalId

import javax.persistence.*

/**
 * Created by ctash on 29.04.2015.
 */
@Entity
class StringHash {

    @Id
    @Column(name = "id")
    @GeneratedValue
    var id: Long? = null

    @Column(name = "value", nullable = false)
    var value: String? = null

    @Column(name = "hash_part_1", nullable = false)
    var hashPart1: Byte? = null

    @Column(name = "hash_part_2", nullable = false)
    var hashPart2: Byte? = null

    @Column(name = "hash_part_3", nullable = false)
    var hashPart3: Byte? = null

    @Column(name = "hash_part_4", nullable = false)
    var hashPart4: Byte? = null

    @OneToMany(mappedBy = "title")
    var isTitleOfDocuments: Collection<Document>? = null

    @OneToMany(mappedBy = "author")
    var isAuthorOfDocuments: Collection<Document>? = null

    @OneToMany(mappedBy = "title")
    var isTitleOfEnrichedDocuments: Collection<EnrichedDocument>? = null

    @OneToMany(mappedBy = "author")
    var isAuthorOfEnrichedDocuments: Collection<EnrichedDocument>? = null
}
