package ru.umeta.libraryintegration.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * Created by ctash on 29.04.2015.
 */
@Entity
data class StringHash (

        @Id
        @Column(name = "id")
        @GeneratedValue
        var id: Long,

        val tokens: Set<String>,

        @Column(name = "hash_part_1", nullable = false)
        val hashPart1: Byte,

        @Column(name = "hash_part_2", nullable = false)
        var hashPart2: Byte,

        @Column(name = "hash_part_3", nullable = false)
        var hashPart3: Byte,

        @Column(name = "hash_part_4", nullable = false)
        var hashPart4: Byte)
