package ru.umeta.libraryintegration.inmemory;

import gnu.trove.map.TIntLongMap;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TIntLongHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.umeta.libraryintegration.dao.StringHashDao;
import ru.umeta.libraryintegration.model.StringHash;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Kirill Kosolapov (https://github.com/c-tash) on 12.11.2015.
 */
@Repository
public class StringHashRepository {

    private TIntLongMap map = new TIntLongHashMap();

    private Map<Long, Set<String>> titleTokenMap = new HashMap<>();
    private Map<Long, Set<String>> authorTokenMap = new HashMap<>();

    private final StringHashDao stringHashDao;

    @Autowired
    public StringHashRepository(StringHashDao stringHashDao) {
        this.stringHashDao = stringHashDao;
    }

    public StringHash get(String string) {
        long id = map.get(string.hashCode());
        if (id == map.getNoEntryValue()) {
            return stringHashDao.get(string);
        } else {
            return stringHashDao.getById(id);
        }

    }

    public Number save(StringHash stringHash) {
        Long id = stringHashDao.save(stringHash);
        map.putIfAbsent(stringHash.getValue().hashCode(), id);
        return id;
    }
}
