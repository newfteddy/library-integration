package ru.umeta.libraryintegration.service

import org.springframework.beans.factory.annotation.Autowired
import ru.umeta.libraryintegration.inmemory.StringHashRepository
import ru.umeta.libraryintegration.model.StringHash
import ru.umeta.libraryintegration.util.MD5To32Algorithm
import java.util.*

/**
 * Created by k.kosolapov on 14.05.2015.
 */
class StringHashService
@Autowired
constructor(private val stringHashRepository: StringHashRepository) {

    private val tokenMap = HashMap<String, Int>()

    fun getStringHash(string: String): StringHash {
        var simHash = 0
        val tokens: Set<String>
        if (string.length < 2) {
            simHash = 0
            tokens = emptySet()
        } else {
            tokens = getSimHashTokens(string)
            /**
             * 32 is hash size
             */
            val preHash = IntArray(32)
            Arrays.fill(preHash, 0)

            for (token in tokens) {
                var tokenHash: Int
                var mapTokenHash: Int? = tokenMap[token]
                if (mapTokenHash == null) {
                    tokenHash = MD5To32Algorithm.getHash(token)
                    tokenMap.put(token, tokenHash)
                } else {
                    tokenHash = mapTokenHash
                }
                for (i in 0..31) {
                    if (tokenHash % 2 != 0) {
                        preHash[i]++
                    } else {
                        preHash[i]--
                    }
                    tokenHash = tokenHash.ushr(1)
                }

            }

            for (i in 0..31) {
                if (preHash[i] >= 0) {
                    simHash++
                }
                simHash *= 2
            }
        }

        val simHashPart4 = (simHash % 256).toByte()
        simHash = simHash.ushr(8)
        val simHashPart3 = (simHash % 256).toByte()
        simHash = simHash.ushr(8)
        val simHashPart2 = (simHash % 256).toByte()
        simHash = simHash.ushr(8)
        val simHashPart1 = (simHash % 256).toByte()

        return StringHash(-1, tokens, simHashPart1, simHashPart2, simHashPart3, simHashPart4)
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

        var repoStringHash: StringHash? = stringHashRepository[string]
        if (repoStringHash == null) {

            val stringHash = getStringHash(string)
            stringHashRepository.save(stringHash, string)
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
