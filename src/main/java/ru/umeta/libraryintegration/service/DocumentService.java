package ru.umeta.libraryintegration.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.BitSet;

/**
 * Created by ctash on 28.04.2015.
 */
public class DocumentService {

    /**
     * Calculates 32-bit SimHash for the string and returns it as an int
     * @param string
     * @return
     */
    public int getSimHash(String string) {
        String[] tokens = string.split("\\s");
        /**
         *  32 is hash size
         */
        int[] preHash = new int[32];
        Arrays.fill(preHash, 0);

        for (String token : tokens) {
            int tokenHash = token.hashCode();
            for (int i = 0; i < 32; i++) {
                if (tokenHash % 2 != 0) {
                    preHash[i]++;
                } else {
                    preHash[i]--;
                }
                tokenHash = tokenHash >>> 1;
            }
        }

        int result = 0;
        for (int i = 0; i < 32; i++) {
            if (preHash[i] >= 0) {
                result++;
            }
            result *= 2;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(Integer.toBinaryString(new DocumentService().getSimHash("собрание сочинений и рассказов")));
        System.out.println(Integer.toBinaryString(new DocumentService().getSimHash("собр. соч. и расск.")));
    }
}
