package org.dao;

import jakarta.persistence.EntityManager;
import org.config.JpaConnection;
import org.model.Disciplina;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

// DAO responsável pelas operações CRUD da entidade Disciplina.
@Repository
public class DisciplinaDao {
    private static final AtomicLong ID_SEQ = new AtomicLong(3000L);

    // Salva uma nova disciplina.
    public Disciplina create(Disciplina disciplina) {
        return JpaConnection.executeInTransaction(entityManager -> {
            entityManager.persist(disciplina);
            return disciplina;
        });
    }

    // Busca uma disciplina pelo ID.
    public Disciplina findById(long id) {
        EntityManager entityManager = JpaConnection.getEntityManager();
        try {
            return entityManager.find(Disciplina.class, id);
        } finally {
            entityManager.close();
        }
    }

    // Lista todas as disciplinas.
    public List<Disciplina> findAll() {
        EntityManager entityManager = JpaConnection.getEntityManager();
        try {
            return entityManager.createQuery("from Disciplina", Disciplina.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    // Atualiza os dados de uma disciplina.
    public Disciplina update(Disciplina disciplina) {
        return JpaConnection.executeInTransaction(entityManager -> entityManager.merge(disciplina));
    }

    // Remove uma disciplina pelo ID e informa se a remoção ocorreu.
    public boolean deleteById(long id) {
        return JpaConnection.executeInTransaction(entityManager -> {
            Disciplina disciplina = entityManager.find(Disciplina.class, id);
            if (disciplina == null) {
                return false;
            }
            entityManager.remove(disciplina);
            return true;
        });
    }

    // Remove todas as disciplinas e retorna a quantidade apagada.
    public int deleteAll() {
        return JpaConnection.executeInTransaction(entityManager ->
                entityManager.createQuery("delete from Disciplina").executeUpdate()
        );
    }

    // Retorna o próximo ID sequencial.
    public long nextId() {
        return ID_SEQ.incrementAndGet();
    }
}
