package ru.umeta.libraryintegration.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.umeta.libraryintegration.dao.StringHashDao;
import ru.umeta.libraryintegration.json.ParseResult;
import ru.umeta.libraryintegration.model.StringHash;

import java.util.*;

/**
 * Created by k.kosolapov on 14.05.2015.
 */
public class StringHashService {

    @Autowired
    private StringHashDao stringHashDao;

    public StringHash getStringHash(String string) {
        int simHash = getSimHash(string);
        byte simHashPart4 = (byte) (simHash % 256);
        simHash = simHash >>> 8;
        byte simHashPart3 = (byte) (simHash % 256);
        simHash = simHash >>> 8;
        byte simHashPart2 = (byte) (simHash % 256);
        simHash = simHash >>> 8;
        byte simHashPart1 = (byte) (simHash % 256);
        StringHash stringHash = new StringHash();
        stringHash.setValue(string);
        stringHash.setHashPart1(simHashPart1);
        stringHash.setHashPart2(simHashPart2);
        stringHash.setHashPart3(simHashPart3);
        stringHash.setHashPart4(simHashPart4);
        return stringHash;
    }

    /**
     * Calculates 32-bit SimHash for the string and returns it as an int
     * @param string
     * @return
     */
    private int getSimHash(String string) {
        if (string == null) {
            return 0;
        }

        if (string.length() < 2) {
            return 0;
        }

        Set<String> tokens = new HashSet<>();
        for (int i = 0; i < string.length() - 1; i++) {
            final String token = string.substring(i, i + 1);
            if (!tokens.contains(token)) {
                tokens.add(token);
            }
        }
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


}
