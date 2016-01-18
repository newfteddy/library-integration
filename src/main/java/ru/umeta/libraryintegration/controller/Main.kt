package ru.umeta.libraryintegration.controller

import ru.umeta.libraryintegration.service.MainService

/**
 * The Main Rest Controller
 * Created by Kirill Kosolapov (https://github.com/c-tash) on 06.04.2015.
 */

fun main(args: Array<String>) {
    MainService.use {
        it.parseDirectory(args[0])
    }
}