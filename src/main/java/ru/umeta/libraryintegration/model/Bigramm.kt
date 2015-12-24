package ru.umeta.libraryintegration.model

/**
 * Created by Kirill Kosolapov (https://github.com/c-tash) on 22.12.2015.
 */
class Bigramm(private val intBase: Int) {


    fun getBytes(): Array<Byte> {
        new Array<Byte>
        (intBase and 0x0000ffff)
    }

    companion object {
        cache:Map<Int,>
    }
}