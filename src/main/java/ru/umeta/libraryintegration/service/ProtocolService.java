package ru.umeta.libraryintegration.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.umeta.libraryintegration.inmemory.ProtocolRepository;
import ru.umeta.libraryintegration.model.Protocol;

import java.util.List;

/**
 * Created by k.kosolapov on 20/05/2015.
 */
public class ProtocolService {

    @Autowired
    private ProtocolRepository protocolRepository;

    public Protocol getFromRepository(String protocolName) {
        Protocol repoProtocol = protocolRepository.get(protocolName);
        if (repoProtocol == null) {
            Protocol protocol = new Protocol();
            protocol.setName(protocolName);
            protocol.setId(protocolRepository.save(protocol).longValue());
            repoProtocol = protocol;
        }
        return repoProtocol;
    }

}
