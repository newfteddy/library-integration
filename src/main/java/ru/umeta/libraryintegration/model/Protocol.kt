package ru.umeta.libraryintegration.model

import java.io.Serializable

/**
 * Created by ctash on 29.04.2015.
 */
class Protocol : Serializable {

    var id: Long? = null

    var name: String? = null

    var document: Collection<Document>? = null

    companion object {
        private val serialVersionUID = 4908088548280013641L
    }
}
