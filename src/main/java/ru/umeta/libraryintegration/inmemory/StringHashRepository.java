package ru.umeta.libraryintegration.inmemory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.umeta.libraryintegration.fs.StringHashFsPersister;
import ru.umeta.libraryintegration.model.StringHash;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Kirill Kosolapov (https://github.com/c-tash) on 12.11.2015.
 */
@Repository
public class StringHashRepository {

    private Map<String, StringHash> map = new HashMap<>();
    private Map<Long, StringHash> idMap = new HashMap<>();

//    private Map<String, Set<String>> tokens = new HashMap<>();

    private final StringHashFsPersister fsPersister;

    private AtomicLong identity = new AtomicLong();

    @Autowired
    public StringHashRepository(StringHashFsPersister fsPersister) {
        this.fsPersister = fsPersister;
        long lastId = fsPersister.fillMaps(map, idMap);
        identity.set(lastId + 1);
        //        fsPersister.fillMap(tokens);
    }

    public StringHash get(String string) {
        return map.get(string);
    }

    public StringHash getById(Long id) {
        return idMap.get(id);
    }

    public void save(StringHash stringHash) {
        stringHash.setId(identity.getAndIncrement());
        map.put(stringHash.getValue(), stringHash);
        idMap.put(stringHash.getId(), stringHash);
        fsPersister.save(stringHash);
    }
}
