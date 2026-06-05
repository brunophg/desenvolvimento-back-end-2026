package org.dao;

import jakarta.persistence.EntityManager;
import org.config.JpaConnection;
import org.model.Participante;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ParticipanteDao {
    private static final AtomicLong ID_SEQ = new AtomicLong(2000L);

    // Salva uma nova.
    public Participante create(Participante participante) {
        return JpaConnection.executeInTransaction(entityManager -> {
            entityManager.persist(participante);
            return participante;
        });
    }

    // Busca pelo ID.
    public Participante findById(long id) {
        EntityManager entityManager = JpaConnection.getEntityManager();
        try {
            return entityManager.find(Participante.class, id);
        } finally {
            entityManager.close();
        }
    }

    // Lista todos.
    public List<Participante> findAll() {
        EntityManager entityManager = JpaConnection.getEntityManager();
        try {
            return entityManager.createQuery("from Participante", Participante.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    // Atualiza os dados.
    public Participante update(Participante participante) {
        return JpaConnection.executeInTransaction(entityManager -> entityManager.merge(participante));
    }

    // Remove pelo ID e informa se a remoção ocorreu.
    public boolean deleteById(long id) {
        return JpaConnection.executeInTransaction(entityManager -> {
            Participante participante = entityManager.find(Participante.class, id);
            if (participante == null) {
                return false;
            }
            entityManager.remove(participante);
            return true;
        });
    }

    // Remove todos e retorna a quantidade apagada.
    public int deleteAll() {
        return JpaConnection.executeInTransaction(entityManager ->
                entityManager.createQuery("delete from Participante").executeUpdate()
        );
    }

    // Retorna o próximo ID sequencial.
    public long nextId() {
        return ID_SEQ.incrementAndGet();
    }
}
