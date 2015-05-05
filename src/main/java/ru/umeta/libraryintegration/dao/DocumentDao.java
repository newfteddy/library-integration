package ru.umeta.libraryintegration.dao;

import ru.umeta.libraryintegration.model.Document;

/**
 * Created by k.kosolapov on 05/05/2015.
 */
public class DocumentDao extends AbstractDao<Document> {

    public DocumentDao() {
        super(Document.class);
    }

}
