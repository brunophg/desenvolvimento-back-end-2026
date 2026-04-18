package org.aula.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

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
            entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, getOverrides());
        }
        return entityManagerFactory;
    }

    private static Map<String, Object> getOverrides() {
        Map<String, Object> overrides = new HashMap<>();
        putIfPresent(overrides, "jakarta.persistence.jdbc.driver", "aula.dbe.jdbc.driver");
        putIfPresent(overrides, "jakarta.persistence.jdbc.url", "aula.dbe.jdbc.url");
        putIfPresent(overrides, "jakarta.persistence.jdbc.user", "aula.dbe.jdbc.user");
        putIfPresent(overrides, "jakarta.persistence.jdbc.password", "aula.dbe.jdbc.password");
        putIfPresent(overrides, "hibernate.dialect", "aula.dbe.hibernate.dialect");
        putIfPresent(overrides, "hibernate.hbm2ddl.auto", "aula.dbe.hibernate.ddl-auto");
        putIfPresent(overrides, "hibernate.show_sql", "aula.dbe.hibernate.show-sql");
        putIfPresent(overrides, "hibernate.format_sql", "aula.dbe.hibernate.format-sql");
        return overrides;
    }

    private static void putIfPresent(Map<String, Object> overrides, String propertyName, String systemPropertyName) {
        String value = System.getProperty(systemPropertyName);
        if (value != null && !value.isBlank()) {
            overrides.put(propertyName, value);
        }
    }
}
