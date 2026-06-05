package org.support;

import jakarta.persistence.EntityManager;
import org.config.JpaConnection;

public final class TestDatabaseSupport {

    private TestDatabaseSupport() {
    }

    public static void clearDatabase() {
        EntityManager entityManager = JpaConnection.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.createQuery("delete from Empresa").executeUpdate();
            entityManager.createQuery("delete from Nota").executeUpdate();
            entityManager.createQuery("delete from ItemNota").executeUpdate();
            entityManager.createQuery("delete from Participante").executeUpdate();
            entityManager.createQuery("delete from Produto").executeUpdate();
            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }
}
