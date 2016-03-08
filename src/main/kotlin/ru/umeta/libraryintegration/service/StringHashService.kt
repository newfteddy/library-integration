package ru.umeta.libraryintegration.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.umeta.libraryintegration.inmemory.RedisRepository
import ru.umeta.libraryintegration.model.StringHash
import ru.umeta.libraryintegration.model.bigrammToInt
import ru.umeta.libraryintegration.util.MD5To32Algorithm
import java.util.*

/**
 * Created by k.kosolapov on 14.05.2015.
 */
@Component
class StringHashService @Autowired constructor(val redisRepository: RedisRepository) {

    public val TROVE_NO_VALUE_INT = 0

    private val tokenMap = HashMap<String, Int>();

    fun getStringHash(string: String): StringHash {
        var simHash = 0
        val tokens: Set<String>
        if (string.length < 2) {
            simHash = 0
            tokens = HashSet<String>(0)
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


        return StringHash(-1, string.hashCode(), simHash, string)
    }

    fun getSimHashTokens(string: String): Set<String> {
        return getTokens(string)
    }

    fun getFromRepository(string: String): Int {
        var string = string
        if (string.length > 255) {
            string = string.substring(0, 255)
        }
        redisRepository.addString(string);
        return -1
    }

    fun getById(id: Long): StringHash {
        //return stringHashRepository.getStringHashById(id);
        throw UnsupportedOperationException();
    }

    fun distance(tokens1: Set<String>, tokens2: Set<String>): Double {
        val union = HashSet<String>(tokens1)
        val intersection = HashSet<String>(tokens1)

        union.addAll(tokens2)
        intersection.retainAll(tokens2)

        return (intersection.size * 1.0) / (union.size * 1.0)
    }

    fun distance(stringId: Long, otherStringId: Long): Double {
        val stringHash = getById(stringId)
        val otherStringHash = getById(otherStringId)
        return 1.0//distance(stringHash.tokens, otherStringHash.tokens)
    }

    fun distance(stringHash: StringHash, otherStringId: Long): Double {
        val otherStringHash = getById(otherStringId)
        return 1.0//distance(stringHash.tokens, otherStringHash.tokens)
    }

    fun getFromRepositoryInit(string: String): Int {
        var string = string
        if (string.length > 255) {
            string = string.substring(0, 255)
        }
        redisRepository.addString(string);
        return -1
    }

}

public fun getTokens(string: String): Set<String> {
    if (string.length == 0) {
        return HashSet()
    }

    val tokens = HashSet<String>()
    for (i in 0..string.length - 1 - 1) {
        val bigramm = string.substring(i, i + 2)
        if (!tokens.contains(bigramm)) {
            tokens.add(bigramm)
        }
    }
    return tokens
}
