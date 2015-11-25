package ru.umeta.libraryintegration.inmemory;

import org.springframework.stereotype.Repository;
import ru.umeta.libraryintegration.model.Protocol;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by k.kosolapov on 11/18/2015.
 */
@Repository
public class ProtocolRepository {

    private AtomicLong identity = new AtomicLong(0);

    private Map<String, Protocol> map = new HashMap<>();

    public Protocol get(String protocolName) {
        return map.get(protocolName);
    }

    public Number save(Protocol protocol) {
        String name = protocol.getName();
        if (map.containsKey(name)) {
            throw new IllegalArgumentException("Protocol with this name already exists.");
        }
        Long id = identity.getAndIncrement();
        protocol.setId(id);
        map.put(name, protocol);
        return id;
    }
}
