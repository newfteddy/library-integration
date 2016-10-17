package ru.umeta.libraryintegration.json

import java.io.Serializable

/**
 * Created by k.kosolapov on 06.04.2015.
 */
class UploadResult(var parsedDocs: Int, var newEnriched: Int) : Serializable {
    companion object {
        private val serialVersionUID = 8711710207201631499L
    }
}
