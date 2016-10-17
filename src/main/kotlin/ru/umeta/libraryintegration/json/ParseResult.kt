package ru.umeta.libraryintegration.json

/**
 * Created by k.kosolapov on 06.04.2015.
 */
abstract class ParseResult protected constructor(
        var title: String,
        var isbn: String?,
        var author: String,
        var publishYear: Int?) {

    abstract fun clone(): ParseResult
}
