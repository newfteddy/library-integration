package ru.umeta.libraryintegration.inmemory

import gnu.trove.map.hash.TIntLongHashMap
import gnu.trove.map.hash.TLongIntHashMap
import gnu.trove.set.hash.TIntHashSet
import ru.umeta.libraryintegration.fs.StringHashFsPersister
import ru.umeta.libraryintegration.model.StringHash
import java.util.*

/**
 * Created by Kirill Kosolapov (https://github.com/c-tash) on 12.11.2015.
 */
object StringHashRepository {

    private val mapHashCodeToId = TIntLongHashMap()
    private val mapIdToSimHash = TLongIntHashMap()
    private val mapIdToTokens = HashMap<Long, TIntHashSet>()

    private var identity: Long = 0

    init {
        val lastId = StringHashFsPersister.fillMaps(mapHashCodeToId, mapIdToSimHash, mapIdToTokens)
        identity = lastId + 1
        //        fsPersister.fillMap(tokens);
    }

    fun getByHashCode(string: String): Long {
        return mapHashCodeToId[string.hashCode()]
    }

    fun getSimHashById(id: Long): Int {
        return mapIdToSimHash[id]
    }

    fun save(stringHash: StringHash, value: String) {
        val id = identity++
        stringHash.id = id
        mapHashCodeToId.put(value.hashCode(), id)
        mapIdToSimHash.put(id, stringHash.simHash)
        mapIdToTokens.put(id, stringHash.tokens)
        StringHashFsPersister.save(stringHash, value)
    }

    fun getStringHashById(id: Long): StringHash {
        val simHash = mapIdToSimHash[id]
        val tokens = mapIdToTokens[id] ?: throw RuntimeException("token set for simHash with id=\"$id\" is null.")
        return StringHash(id, tokens, simHash)
    }
}
