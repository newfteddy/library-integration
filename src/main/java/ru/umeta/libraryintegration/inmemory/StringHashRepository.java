package ru.umeta.libraryintegration.inmemory;

import gnu.trove.map.TIntLongMap;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TIntLongHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.umeta.libraryintegration.dao.StringHashDao;
import ru.umeta.libraryintegration.model.StringHash;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Kirill Kosolapov (https://github.com/c-tash) on 12.11.2015.
 */
@Repository
public class StringHashRepository {

    private Map<String, StringHash> map = new HashMap<>();

    private Map<String, Set<String>> tokens = new HashMap<>();

    private List<StringHash> toBePersisted = new ArrayList<>();

    private static final int HIBERNATE_BATCH_SIZE = 10000;

    private final StringHashDao stringHashDao;

    @Autowired
    public StringHashRepository(StringHashDao stringHashDao) {
        this.stringHashDao = stringHashDao;
    }

    public StringHash get(String string) {
        return map.get(string);
    }

    public void save(StringHash stringHash) {
        toBePersisted.add(stringHash);
        if (HIBERNATE_BATCH_SIZE <= toBePersisted.size()) {
            persistTransient();
        }
        map.put(stringHash.getValue(), stringHash);

    }

    public void persistTransient() {
        try {
            stringHashDao.persistBatch(toBePersisted);
        } catch (Exception e) {
            e.printStackTrace();
        }

        toBePersisted = new ArrayList<>();
    }
}
