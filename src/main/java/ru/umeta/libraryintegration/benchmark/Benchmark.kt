package ru.umeta.libraryintegration.benchmark

import ru.umeta.libraryintegration.service.DocumentService
import java.util.*

/**
 * Created by k.kosolapov on 12/17/2015.
 */
fun main(args: Array<String>) {
    val str = "????????? ?. ?. ??????? ?. ?. ????????-????????? ?. ?. ????. ???. ? ??????. ???? ????. ??????.-?????"
    for (rightBorder in 9..99) {
        val subString = str.subSequence(0..rightBorder)
        val subStringMut:StringBuilder = subString as StringBuilder
        for (i in 0..999) {
            for (j in 0..rightBorder/2) {
                val rnd = Random()
                var noiseIndex = rnd.nextInt(rightBorder+1)
                subStringMut.setCharAt(noiseIndex, 'ð' + j)
            }
        }

    }
}
