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
@Autowired
constructor(private val fsPersister: StringHashFsPersister) {

    private val mapHashCodeToId = TIntLongHashMap();
    private val mapIdToSimHash = TLongIntHashMap();
    private val mapIdToTokens = HashMap<Long, TIntHashSet>();

    private var identity: Long = 0

    init {
        val lastId = fsPersister.fillMaps(mapHashCodeToId, mapIdToSimHash, mapIdToTokens)
        identity = lastId + 1
        //        fsPersister.fillMap(tokens);
    }

    fun getByHashCode(string: String): Long {
        return mapHashCodeToSimHash[string.hashCode()]
    }

    fun getSimHashById(id: Long): Int {
        return mapIdToSimHash[id]
    }

    fun save(stringHash: StringHash, value: String) {
        stringHash.id = identity++
        mapHashCodeToSimHash.put(value.hashCode(), stringHash)
        mapIdToSimHash.put(stringHash.id, stringHash)
        fsPersister.save(stringHash, value)
    }
}
