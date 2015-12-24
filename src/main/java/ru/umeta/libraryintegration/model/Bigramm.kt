package ru.umeta.libraryintegration.model

/**
 * Bigramm related functions. A bigramm is a sequence of exactly **two** characters. It is stored as an
 * [Int]
 * Created by Kirill Kosolapov (https://github.com/c-tash) on 22.12.2015.
 */

/**
 * Gets [Char] array from [Int] representation
 * @param intBase the representation of bigramm
 * @return an array of **two** chars for the value of int
 */
fun getBigrammChars(intBase: Int): CharArray {
    val char1: Char = (intBase and 0x0000FFFF).toChar()
    val char2: Char = ((intBase and 0xFFFF0000.toInt()) ushr 16).toChar()
    val result = CharArray(2);
    result[0] = char1;
    result[1] = char2;
    return result;
}

/**
 * Constructs an Int representation of Bigramm for input string.
 * @param str the input string
 * @return an Int representation of Bigramm
 * @throws IllegalArgumentException if str has not exactly two characters
 */
fun bigrammToInt(str: String): Int {
    if (str.length != 2) {
        throw IllegalArgumentException("Tried to create bigramm from string of length not equal to 2.")
    }
    val char1 = str[0]
    val char2 = str[1]
    var result: Int = char1.toInt();
    result += char2.toInt() shl 16;
    return result;
}
