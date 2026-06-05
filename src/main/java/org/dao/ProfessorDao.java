package org.dao;

import jakarta.persistence.EntityManager;
import org.config.JpaConnection;
import org.model.Professor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

// DAO responsável pelas operações CRUD da entidade Professor.
@Repository
public class ProfessorDao {
    private static final AtomicLong ID_SEQ = new AtomicLong(4000L);

    // Salva um novo professor.
    public Professor create(Professor professor) {
        return JpaConnection.executeInTransaction(entityManager -> {
            entityManager.persist(professor);
            return professor;
        });
    }

    // Busca um professor pelo ID.
    public Professor findById(long id) {
        EntityManager entityManager = JpaConnection.getEntityManager();
        try {
            return entityManager.find(Professor.class, id);
        } finally {
            entityManager.close();
        }
    }

    // Lista todos os professores.
    public List<Professor> findAll() {
        EntityManager entityManager = JpaConnection.getEntityManager();
        try {
            return entityManager.createQuery("from Professor", Professor.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    // Atualiza os dados de um professor.
    public Professor update(Professor professor) {
        return JpaConnection.executeInTransaction(entityManager -> entityManager.merge(professor));
    }

    // Remove um professor pelo ID e informa se a remoção ocorreu.
    public boolean deleteById(long id) {
        return JpaConnection.executeInTransaction(entityManager -> {
            Professor professor = entityManager.find(Professor.class, id);
            if (professor == null) {
                return false;
            }
            entityManager.remove(professor);
            return true;
        });
    }

    // Remove todos os professores e retorna a quantidade apagada.
    public int deleteAll() {
        return JpaConnection.executeInTransaction(entityManager ->
                entityManager.createQuery("delete from Professor").executeUpdate()
        );
    }

    // Retorna o próximo ID sequencial.
    public long nextId() {
        return ID_SEQ.incrementAndGet();
    }
}
