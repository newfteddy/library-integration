package ru.umeta.libraryintegration.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;
import ru.umeta.libraryintegration.model.Document;

import java.io.Serializable;

/**
 * Created by k.kosolapov on 30.04.2015.
 */

public class DocumentDao implements IDocumentDao<Document, String> {

    private Session session;

    private Transaction transaction;

    public DocumentDao() {
    }

    public Session openSession() {
        session = getSessionFactory().openSession();
        return session;
    }

    public void closeSession() {
        session.close();
    }

    private static SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        
    }
}
