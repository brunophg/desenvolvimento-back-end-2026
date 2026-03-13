package org.aula.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

// Centraliza a criação e o fechamento de conexões JPA.
public final class JpaConnection {

    private static final String PERSISTENCE_UNIT = "aula-dbe-pu";
    private static EntityManagerFactory entityManagerFactory;

    private JpaConnection() {
    }

    // Fornece um EntityManager para operações no banco.
    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    // Fecha o EntityManagerFactory ao encerrar a aplicação.
    public static void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

    private static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
            entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        }
        return entityManagerFactory;
    }
}
