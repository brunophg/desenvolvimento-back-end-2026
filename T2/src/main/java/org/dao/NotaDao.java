package org.dao;

import jakarta.persistence.EntityManager;
import org.config.JpaConnection;
import org.model.Nota;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class NotaDao {
    private static final AtomicLong ID_SEQ = new AtomicLong(2000L);

    // Salva uma nova.
    public Nota create(Nota nota) {
        return JpaConnection.executeInTransaction(entityManager -> {
            entityManager.persist(nota);
            return nota;
        });
    }

    // Busca pelo ID.
    public Nota findById(long id) {
        EntityManager entityManager = JpaConnection.getEntityManager();
        try {
            return entityManager.find(Nota.class, id);
        } finally {
            entityManager.close();
        }
    }

    // Lista todos.
    public List<Nota> findAll() {
        EntityManager entityManager = JpaConnection.getEntityManager();
        try {
            return entityManager.createQuery("from Nota", Nota.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    // Atualiza os dados.
    public Nota update(Nota nota) {
        return JpaConnection.executeInTransaction(entityManager -> entityManager.merge(nota));
    }

    // Remove pelo ID e informa se a remoção ocorreu.
    public boolean deleteById(long id) {
        return JpaConnection.executeInTransaction(entityManager -> {
            Nota nota = entityManager.find(Nota.class, id);
            if (nota == null) {
                return false;
            }
            entityManager.remove(nota);
            return true;
        });
    }

    // Remove todos e retorna a quantidade apagada.
    public int deleteAll() {
        return JpaConnection.executeInTransaction(entityManager ->
                entityManager.createQuery("delete from Nota").executeUpdate()
        );
    }

    // Retorna o próximo ID sequencial.
    public long nextId() {
        return ID_SEQ.incrementAndGet();
    }
}
