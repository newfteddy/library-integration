package ru.umeta.libraryintegration.model

import java.util.*

/**
 * Created by ctash on 30.04.2015.
 */
data class EnrichedDocument(
        var id: Long,

        val title: Long,

        val author: Long,

        var isbn: Int,

        var publishYear: Int)

