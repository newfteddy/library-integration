package ru.umeta.libraryintegration.model

import java.nio.ByteBuffer

/**
 * Created by k.kosolapov on 11/26/2015.
 */
data class Duplicate(
        val document: EnrichedDocumentLite,
        val measure: Double)