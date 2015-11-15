package ru.umeta.libraryintegration.inmemory;

import org.springframework.stereotype.Repository;
import ru.umeta.libraryintegration.model.StringHash;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Kirill Kosolapov (https://github.com/c-tash) on 12.11.2015.
 */
@Repository
public class StringHashRepository {

    ConcurrentMap<String, StringHash> map = new ConcurrentHashMap<>();

    public StringHash get(String string) {
        return map.get(string);
    }

    public Number save(StringHash stringHash) {
        map.putIfAbsent(stringHash.getValue(), stringHash);
        return null;
    }
}
