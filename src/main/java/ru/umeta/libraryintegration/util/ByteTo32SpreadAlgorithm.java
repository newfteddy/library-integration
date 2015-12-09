package ru.umeta.libraryintegration.util;

/**
 * Created by k.kosolapov on 12/8/2015.
 */
public class ByteTo32SpreadAlgorithm {
    public static int getHash(String value) {
        char[] chars = value.toCharArray();
        int result = 0;
        for (char character : chars) {
            byte b;
            result = (result << 8) + (byte) character;
        }
        //if the last bit is one shift it to the left on 16 bits.
        if ((result & 1) == 1) {
            result = result << 16;
        }
        return result;
    }
}
