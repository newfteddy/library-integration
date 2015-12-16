package ru.umeta.libraryintegration.inmemory

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
constructor(//    private Map<String, Set<String>> tokens = new HashMap<>();

        private val fsPersister: StringHashFsPersister) {

    private val map = HashMap<String, StringHash>()
    private val idMap = HashMap<Long, StringHash>()

    private var identity: Long = 0

    init {
        val lastId = fsPersister.fillMaps(map, idMap)
        identity = lastId + 1
        //        fsPersister.fillMap(tokens);
    }

    operator fun get(string: String): StringHash? {
        return map[string]
    }

    fun getById(id: Long?): StringHash? {
        return idMap[id]
    }

    fun save(stringHash: StringHash) {
        stringHash.id = identity++
        map.put(stringHash.value, stringHash)
        idMap.put(stringHash.id, stringHash)
        fsPersister.save(stringHash)
    }
}
