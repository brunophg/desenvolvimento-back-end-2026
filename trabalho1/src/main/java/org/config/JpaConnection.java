package org.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.function.Consumer;
import java.util.function.Function;

// Centraliza a criação e o fechamento de conexões JPA.
public final class JpaConnection {

    private static final String PERSISTENCE_UNIT = "trabalho1";
    private static EntityManagerFactory entityManagerFactory;

    private JpaConnection() {
    }

    // Fornece um EntityManager para operações no banco.
    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    // Executa uma operação em transação local com rollback automático em caso de erro.
    public static <T> T executeInTransaction(Function<EntityManager, T> action) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();
            T result = action.apply(entityManager);
            entityManager.getTransaction().commit();
            return result;
        } catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    // Variante sem retorno para ações transacionais.
    public static void runInTransaction(Consumer<EntityManager> action) {
        executeInTransaction(entityManager -> {
            action.accept(entityManager);
            return null;
        });
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
