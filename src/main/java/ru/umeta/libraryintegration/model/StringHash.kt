package ru.umeta.libraryintegration.model

import org.hibernate.annotations.NaturalId

import javax.persistence.*

/**
 * Created by ctash on 29.04.2015.
 */
@Entity
class StringHash constructor(
        @Column(name = "value", nullable = false)
        val value: String,

        @Column(name = "hash_part_1", nullable = false)
        val hashPart1: Byte,

        @Column(name = "hash_part_2", nullable = false)
        var hashPart2: Byte,

        @Column(name = "hash_part_3", nullable = false)
        var hashPart3: Byte,

        @Column(name = "hash_part_4", nullable = false)
        var hashPart4: Byte) {

    @Id
    @Column(name = "id")
    @GeneratedValue
    var id: Long? = null
}
