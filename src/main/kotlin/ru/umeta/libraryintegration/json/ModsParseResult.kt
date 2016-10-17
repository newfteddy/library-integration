package ru.umeta.libraryintegration.json

import gov.loc.mods.v3.ModsDefinition

/**
 * Created by Kirill Kosolapov (https://github.com/c-tash) on 14/04/2015.
 */
class ModsParseResult(title: String,
                      isbn: String,
                      author: String,
                      publishYear: Int?,
                      var modsDefinition: ModsDefinition?)
: ParseResult(title, isbn, author, publishYear) {

    override fun clone(): ParseResult {
        return ModsParseResult(title, isbn, author, publishYear, modsDefinition)
    }
}
