package ru.umeta.libraryintegration.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.umeta.libraryintegration.inmemory.RedisRepository
import ru.umeta.libraryintegration.model.StringHash
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


        return StringHash(simHash)
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
        var resultString = string
        if (resultString.length > 255) {
            resultString = resultString.substring(0, 255)
        }
        redisRepository.addString(resultString);
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

public var distanceCounterAll = 0;
public var distanceCounterOptim1 = 0;
public var distanceCounterOptim2 = 0;

public fun distanceWithTheorems(string1: String,
                                string2: String,
                                preCalculatedTokens1: List<Bigramm>? = null,
                                preCalculatedTokens2: List<Bigramm>? = null,
                                threshold: Double = 0.7):
        Double {
    var length1 = string1.length
    var length2 = string2.length
    val ratio: Double
    distanceCounterAll++

    if (length1 < length2) {
        ratio = (1 - threshold) * (length1 - 1) + 1;
        if (length1 < 1 + threshold * (length2 - 1)) {
            distanceCounterOptim1++
            return 0.0
        }
    } else {
        ratio = (1 - threshold) * (length2 - 1) + 1;
        if (length2 < 1 + threshold * (length1 - 1)) {
            distanceCounterOptim1++
            return 0.0
        }
    }

    val tokens1 = preCalculatedTokens1 ?: getBigrammWeighted(string1)
    val tokens2 = preCalculatedTokens2 ?: getBigrammWeighted(string2)
    var index1 = 0
    var index2 = 0
    var sameCount = 0
    var count1 = 0
    var count2 = 0
    while (index1 < tokens1.size && index2 < tokens2.size) {
        val bigramm1 = tokens1[index1]
        val bigramm2 = tokens2[index2]
        if (bigramm1.value.equals(bigramm2.value)) {
            sameCount += Math.min(bigramm1.count, bigramm2.count)
            index1++
            index2++
            count1 += bigramm1.count
            count2 += bigramm1.count
        } else {
            if (bigramm1.value.compareTo(bigramm2.value) < 0) {
                index1++
                count1 += bigramm1.count
            } else {
                index2++
                count2 += bigramm2.count
            }
        }
        if (((count1 > ratio) || (count2 > ratio)) && (sameCount == 0)) {
            distanceCounterOptim2++
            return 0.0
        }
    }
    return (sameCount * 1.0)/( length1-1 + length2-1 - sameCount)
}

class Bigramm : Comparable<Bigramm> {
    val value: String
    var count: Int

    constructor(value: String, count: Int) {
        this.value = value
        this.count = count
    }


    override fun compareTo(other: Bigramm): Int {
        return value.compareTo(other.value)
    }

}


public fun getBigrammWeighted(string: String): List<Bigramm> {
    if (string.length == 0) {
        return emptyList();
    }

    val tokens = HashMap<String, Bigramm>()

    for (i in 0..string.length - 1 - 1) {
        val bigramm = string.substring(i, i + 2)
        val mapBigramm = tokens[bigramm];
        if (mapBigramm != null) {
            mapBigramm.count++;
        } else {
            tokens[bigramm] = Bigramm(bigramm, 1)
        }
    }

    val result = tokens.values.toList()
    Collections.sort(result)
    return result
}