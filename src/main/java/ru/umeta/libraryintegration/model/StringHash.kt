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

        val tokens: Set<Bigramm>,

        val simHash: Int)
