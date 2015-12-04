package ru.umeta.libraryintegration.inmemory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.umeta.libraryintegration.dao.StringHashDao;
import ru.umeta.libraryintegration.fs.StringHashFsSaver;
import ru.umeta.libraryintegration.model.StringHash;

import java.util.*;

/**
 * Created by Kirill Kosolapov (https://github.com/c-tash) on 12.11.2015.
 */
@Repository
public class StringHashRepository {

    private Map<String, StringHash> map = new HashMap<>();

    private Map<String, Set<String>> tokens = new HashMap<>();

    private final StringHashFsSaver fsSaver;

    @Autowired
    public StringHashRepository(StringHashFsSaver fsSaver) {
        this.fsSaver = fsSaver;
    }

    public StringHash get(String string) {
        return map.get(string);
    }

    public void save(StringHash stringHash) {
        map.put(stringHash.getValue(), stringHash);
        fsSaver.save(stringHash);
    }
}
