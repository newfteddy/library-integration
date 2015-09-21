package ru.umeta.libraryintegration.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.umeta.libraryintegration.dao.StringHashDao;
import ru.umeta.libraryintegration.json.ParseResult;
import ru.umeta.libraryintegration.model.StringHash;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

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

        if (string.length() < 4) {
            return 0;
        }

        Set<String> tokens = getTokens(string);
        /**
         *  32 is hash size
         */
        int[] preHash = new int[32];
        Arrays.fill(preHash, 0);

        for (String token : tokens) {
            int tokenHash = getHash(token);
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

    private int getHash(String value) {
        try {
            byte[] bytes = value.getBytes("UTF-8");
            int result = 0;
            for (byte oneByte : bytes) {
                result = result*31 + (oneByte & 0xFF);
            }
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private Set<String> getTokens(String string) {
        if (string == null || string.length() == 0) {
            return null;
        }

        Set<String> tokens = new HashSet<>();
        for (int i = 0; i < string.length() - 3; i++) {
            final String token = string.substring(i, i + 4);
            if (!tokens.contains(token)) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    public synchronized StringHash getFromRepository(String string) {
        if (string.length() > 255) {
            string = string.substring(0, 255);
        }

        StringHash repoStringHash = stringHashDao.get(string);
        if (repoStringHash == null) {
            StringHash stringHash = getStringHash(string);
            stringHash.setId(stringHashDao.save(stringHash).longValue());
            repoStringHash = stringHash;
        }
        return repoStringHash;
    }

    public double distance(String obj1, String obj2) {
        Set<String> tokens1 = null;
        Set<String> tokens2 = null;
        if (obj1.length() <= 4 || obj2.length() <= 4) {
            tokens1 = getCharTokens(obj1);
            tokens2 = getCharTokens(obj2);
        } else {
            tokens1 = getTokens(obj1);
            tokens2 = getTokens(obj2);
        }


        if (tokens1 == null) {
            if (tokens2 == null) {
                return 1;
            }
            return 0;
        }

        if (tokens2 == null) {
            return 0;
        }

        Set<String> union = new HashSet<>(tokens1);
        Set<String> intersection = new HashSet<>(tokens1);

        union.addAll(tokens2);
        intersection.retainAll(tokens2);

        return (intersection.size()*1.)/(union.size()*1.);
    }

    private Set<String> getCharTokens(String string) {
        Set<String> charTokens = new HashSet<>();

        int length = string.length();
        for (int i = 0; i < length; i++) {
            String charToken = String.valueOf(string.charAt(i));
            if (!charTokens.contains(charToken)) {
                charTokens.add(charToken);
            }
        }

        return charTokens;
    }
}
