package ru.umeta.libraryintegration.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.umeta.libraryintegration.dao.ProtocolDao;
import ru.umeta.libraryintegration.model.Document;
import ru.umeta.libraryintegration.model.EnrichedDocument;
import ru.umeta.libraryintegration.model.Protocol;

import java.util.List;

/**
 * Created by k.kosolapov on 20/05/2015.
 */
public class ProtocolService {

    @Autowired
    private ProtocolDao protocolDao;

    public Protocol getFromRepository(String protocolName) {
        Protocol repoProtocol = protocolDao.get(protocolName);
        if (repoProtocol == null) {
            Protocol protocol = new Protocol();
            protocol.setName(protocolName);
            repoProtocol = protocolDao.save(protocol);
        }
        return repoProtocol;
    }

    public List<EnrichedDocument> findNearDuplicates(Document document) {

    }
}
