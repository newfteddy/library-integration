package ru.umeta.libraryintegration.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.umeta.libraryintegration.inmemory.StringHashRepository;
import ru.umeta.libraryintegration.model.StringHash;
import ru.umeta.libraryintegration.util.ByteTo32SpreadAlgorithm;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by k.kosolapov on 14.05.2015.
 */
public class StringHashService {

    private final StringHashRepository stringHashRepository;

    private final Map<String, Integer> tokenMap = new HashMap<>();

    @Autowired
    public StringHashService(StringHashRepository stringHashRepository) {
        this.stringHashRepository = stringHashRepository;
    }

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

        Set<String> tokens = getSimHashTokens(string);
        /**
         *  32 is hash size
         */
        int[] preHash = new int[32];
        Arrays.fill(preHash, 0);

        for (String token : tokens) {
            Integer tokenHash = tokenMap.get(token);
            if (tokenHash == null) {
                tokenHash = ByteTo32SpreadAlgorithm.getHash(token);
                tokenMap.put(token, tokenHash);
            }
            if (((tokenHash >>> 16) & 1) == 1) {
                tokenHash >>>= 16;
                for (int i = 16; i < 32; i++) {
                    if (tokenHash % 2 != 0) {
                        preHash[i]++;
                    } else {
                        preHash[i]--;
                    }
                    tokenHash = tokenHash >>> 1;
                }
            } else {
                for (int i = 0; i < 16; i++) {
                    if (tokenHash % 2 != 0) {
                        preHash[i]++;
                    } else {
                        preHash[i]--;
                    }
                    tokenHash = tokenHash >>> 1;
                }
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



    public Set<String> getSimHashTokens(String string) {
        return getTokens(string);
    }

    public Set<String> getTokens(String string) {
        if (string == null) {
            return Collections.emptySet();
        }
        return getShortTokens(string);
    }

    private Set<String> getShortTokens(String string) {
        if (string == null || string.length() == 0) {
            return null;
        }

        Set<String> tokens = new HashSet<>();
        for (int i = 0; i < string.length() - 1; i++) {
            final String token = string.substring(i, i + 2);
            if (!tokens.contains(token)) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    public StringHash getFromRepository(String string) {
        if (string.length() > 255) {
            string = string.substring(0, 255);
        }

        StringHash repoStringHash = stringHashRepository.get(string);
        if (repoStringHash == null) {

            StringHash stringHash = getStringHash(string);
            stringHashRepository.save(stringHash);
            repoStringHash = stringHash;
        }
        return repoStringHash;
    }

    public double distance(Set<String> tokens1, Set<String> tokens2) {
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
}
