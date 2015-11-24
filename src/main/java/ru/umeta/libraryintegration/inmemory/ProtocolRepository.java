package ru.umeta.libraryintegration.inmemory;

import org.springframework.stereotype.Repository;
import ru.umeta.libraryintegration.model.Protocol;

/**
 * Created by Kirill Kosolapov (https://github.com/c-tash) on 18.11.2015.
 */
@Repository
public class ProtocolRepository {
    public static final Protocol protocol = new Protocol();

    public Protocol get(String protocolName) {
        return protocol;
    }

    public Number save(Protocol protocol) {
        return 1;
    }
}
