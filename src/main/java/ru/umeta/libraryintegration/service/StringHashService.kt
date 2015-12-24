package ru.umeta.libraryintegration.service

import org.springframework.beans.factory.annotation.Autowired
import ru.umeta.libraryintegration.inmemory.StringHashRepository
import ru.umeta.libraryintegration.model.StringHash
import ru.umeta.libraryintegration.model.bigrammToInt
import ru.umeta.libraryintegration.model.getBigrammChars
import ru.umeta.libraryintegration.util.MD5To32Algorithm
import java.util.*

/**
 * Created by k.kosolapov on 14.05.2015.
 */
class StringHashService
@Autowired
constructor(private val stringHashRepository: StringHashRepository) {

    private val tokenMap = HashMap<Int, Int>()

    fun getStringHash(string: String): StringHash {
        var simHash = 0
        val tokens: Set<Int>
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


        return StringHash(-1, tokens, simHash)
    }

    fun getSimHashTokens(string: String): Set<Int> {
        return getTokens(string)
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

    fun distance(tokens1: Set<Int>, tokens2: Set<Int>): Double {
        val union = HashSet(tokens1)
        val intersection = HashSet(tokens1)

        union.addAll(tokens2)
        intersection.retainAll(tokens2)

        return (intersection.size * 1.0) / (union.size * 1.0)
    }
}

public fun getTokens(string: String?): Set<Int> {
    if (string == null || string.length == 0) {
        return emptySet()
    }

    val tokens = HashSet<Int>()
    for (i in 0..string.length - 1 - 1) {
        val bigramm = bigrammToInt(string.substring(i, i + 2));
        if (!tokens.contains(bigramm)) {
            tokens.add(bigramm)
        }
    }
    return tokens
}
