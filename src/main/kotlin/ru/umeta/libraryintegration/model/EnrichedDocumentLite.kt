package ru.umeta.libraryintegration.model

import java.nio.ByteBuffer

/**
 * Created by k.kosolapov on 11/26/2015.
 */
data class EnrichedDocumentLite(
        val id: Int,
        val authorId: Int,
        val authorHash: Int,
        val titleId: Int,
        val titleHash: Int,
        val isbn: Int,
        val publishYear: Int) {

    fun toByteArray(): ByteArray {
        return ByteBuffer.allocate(4 * 6)
                .putInt(authorId).putInt(authorHash)
                .putInt(titleId).putInt(titleHash)
                .putInt(isbn).putInt(publishYear)
                .array()
    }

    companion object {
        fun fromByteArray (id: Int, array: ByteArray): EnrichedDocumentLite{
            val buffer = ByteBuffer.wrap(array)
            val authorId = buffer.getInt()
            val authorHash = buffer.getInt()
            val titleId = buffer.getInt()
            val titleHash = buffer.getInt()
            val isbn = buffer.getInt()
            val publishYear = buffer.getInt()
            return EnrichedDocumentLite(id, authorId, authorHash, titleId, titleHash, isbn, publishYear)
        }
    }
}
