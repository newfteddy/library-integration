package ru.umeta.libraryintegration.controller

import ru.umeta.libraryintegration.json.UploadResult
import ru.umeta.libraryintegration.service.MainService
import ru.umeta.libraryintegration.service.StringHashService
import ru.umeta.libraryintegration.service.getTokens
import java.util.*

/**
 * The Main Rest Controller
 * Created by Kirill Kosolapov (https://github.com/c-tash) on 06.04.2015.
 */

fun main(args: Array<String>) {
    MainService().parseDirectory(args[0])
}