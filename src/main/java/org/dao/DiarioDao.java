package org.dao;

import jakarta.persistence.EntityManager;
import org.config.JpaConnection;
import org.model.Diario;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

// DAO responsável pelas operações CRUD da entidade Diario.
@Repository
public class DiarioDao {
    private static final AtomicLong ID_SEQ = new AtomicLong(6000L);

    // Salva um novo diário.
    public Diario create(Diario diario) {
        return JpaConnection.executeInTransaction(entityManager -> {
            entityManager.persist(diario);
            return diario;
        });
    }

    // Busca um diário pelo ID.
    public Diario findById(long id) {
        EntityManager entityManager = JpaConnection.getEntityManager();
        try {
            return entityManager.find(Diario.class, id);
        } finally {
            entityManager.close();
        }
    }

    // Lista todos os diários.
    public List<Diario> findAll() {
        EntityManager entityManager = JpaConnection.getEntityManager();
        try {
            return entityManager.createQuery("from Diario", Diario.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    // Atualiza os dados de um diário.
    public Diario update(Diario diario) {
        return JpaConnection.executeInTransaction(entityManager -> entityManager.merge(diario));
    }

    // Remove um diário pelo ID e informa se a remoção ocorreu.
    public boolean deleteById(long id) {
        return JpaConnection.executeInTransaction(entityManager -> {
            Diario diario = entityManager.find(Diario.class, id);
            if (diario == null) {
                return false;
            }
            entityManager.remove(diario);
            return true;
        });
    }

    // Remove todos os diários e retorna a quantidade apagada.
    public int deleteAll() {
        return JpaConnection.executeInTransaction(entityManager ->
                entityManager.createQuery("delete from Diario").executeUpdate()
        );
    }

    // Retorna o próximo ID sequencial.
    public long nextId() {
        return ID_SEQ.incrementAndGet();
    }
}
