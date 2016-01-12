package ru.umeta.libraryintegration.inmemory

import gnu.trove.map.hash.TIntIntHashMap
import gnu.trove.map.hash.TIntLongHashMap
import gnu.trove.map.hash.TLongIntHashMap
import gnu.trove.map.hash.TLongLongHashMap
import gnu.trove.set.TIntSet
import gnu.trove.set.hash.TIntHashSet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import ru.umeta.libraryintegration.fs.StringHashFsPersister
import ru.umeta.libraryintegration.model.StringHash

import java.util.*
import java.util.concurrent.atomic.AtomicLong

/**
 * Created by Kirill Kosolapov (https://github.com/c-tash) on 12.11.2015.
 */
@Repository
class StringHashRepository
constructor(private val fsPersister: StringHashFsPersister = StringHashFsPersister()) {

    private val mapHashCodeToId = TIntLongHashMap()
    private val mapIdToSimHash = TLongIntHashMap()
    private val mapIdToTokens = HashMap<Long, TIntHashSet>()

    private var identity: Long = 0

    init {
        val lastId = fsPersister.fillMaps(mapHashCodeToId, mapIdToSimHash, mapIdToTokens)
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
        fsPersister.save(stringHash, value)
    }

    fun getStringHashById(id: Long): StringHash {
        val simHash = mapIdToSimHash[id]
        val tokens = mapIdToTokens[id] ?: throw RuntimeException("token set for simHash with id=\"$id\" is null.")
        return StringHash(id, tokens, simHash)
    }
}
