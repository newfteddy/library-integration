package ru.umeta.libraryintegration.service

import org.springframework.beans.factory.annotation.Autowired
import ru.umeta.libraryintegration.inmemory.StringHashRepository
import ru.umeta.libraryintegration.model.StringHash
import ru.umeta.libraryintegration.util.ByteTo32SpreadAlgorithm
import java.util.*

/**
 * Created by k.kosolapov on 14.05.2015.
 */
class StringHashService
@Autowired
constructor(private val stringHashRepository: StringHashRepository) {

    private val tokenMap = HashMap<String, Int>()

    fun getStringHash(string: String): StringHash {
        var simHash = getSimHash(string)
        val simHashPart4 = (simHash % 256).toByte()
        simHash = simHash.ushr(8)
        val simHashPart3 = (simHash % 256).toByte()
        simHash = simHash.ushr(8)
        val simHashPart2 = (simHash % 256).toByte()
        simHash = simHash.ushr(8)
        val simHashPart1 = (simHash % 256).toByte()

        return StringHash(string, simHashPart1, simHashPart2, simHashPart3, simHashPart4)
    }

    /**
     * Calculates 32-bit SimHash for the string and returns it as an int
     * @param string
     * *
     * @return
     */
    private fun getSimHash(string: String?): Int {
        if (string == null || string.length < 2) {
            return 0
        }

        val tokens = getSimHashTokens(string)
        /**
         * 32 is hash size
         */
        val preHash = IntArray(32)
        Arrays.fill(preHash, 0)

        for (token in tokens) {
            var tokenHash: Int = tokenMap[token]
            if (tokenHash == null) {
                tokenHash = ByteTo32SpreadAlgorithm.getHash(token)
                tokenMap.put(token, tokenHash)
            }
            if (((tokenHash.ushr(16)) and 1) == 1) {
                tokenHash = tokenHash ushr 16
                for (i in 16..31) {
                    if (tokenHash!! % 2 != 0) {
                        preHash[i]++
                    } else {
                        preHash[i]--
                    }
                    tokenHash = tokenHash.ushr(1)
                }
            } else {
                for (i in 0..15) {
                    if (tokenHash!! % 2 != 0) {
                        preHash[i]++
                    } else {
                        preHash[i]--
                    }
                    tokenHash = tokenHash.ushr(1)
                }
            }

        }

        var result = 0
        for (i in 0..31) {
            if (preHash[i] >= 0) {
                result++
            }
            result *= 2
        }
        return result
    }


    fun getSimHashTokens(string: String): Set<String> {
        return getTokens(string)
    }

    fun getTokens(string: String?): Set<String> {
        if (string == null) {
            return emptySet()
        }
        return getShortTokens(string)
    }

    private fun getShortTokens(string: String?): Set<String> {
        if (string == null || string.length == 0) {
            return emptySet();
        }

        val tokens = HashSet<String>()
        for (i in 0..string.length - 1 - 1) {
            val token = string.substring(i, i + 2)
            if (!tokens.contains(token)) {
                tokens.add(token)
            }
        }
        return tokens
    }

    fun getFromRepository(string: String): StringHash {
        var string = string
        if (string.length > 255) {
            string = string.substring(0, 255)
        }

        var repoStringHash: StringHash? = stringHashRepository.get(string)
        if (repoStringHash == null) {

            val stringHash = getStringHash(string)
            stringHashRepository.save(stringHash)
            repoStringHash = stringHash
        }
        return repoStringHash
    }

    fun distance(tokens1: Set<String>, tokens2: Set<String>): Double {
        val union = HashSet(tokens1)
        val intersection = HashSet(tokens1)

        union.addAll(tokens2)
        intersection.retainAll(tokens2)

        return (intersection.size * 1.0) / (union.size * 1.0)
    }
}
