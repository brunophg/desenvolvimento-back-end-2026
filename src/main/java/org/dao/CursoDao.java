package org.dao;

import jakarta.persistence.EntityManager;
import org.config.JpaConnection;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

// DAO responsável pelas operações CRUD da entidade Curso.
@Repository
public class CursoDao {
    private static final AtomicLong ID_SEQ = new AtomicLong(2000L);

    // Salva um novo curso.
    public Curso create(Curso curso) {
        return JpaConnection.executeInTransaction(entityManager -> {
            entityManager.persist(curso);
            return curso;
        });
    }

    // Busca um curso pelo ID.
    public Curso findById(long id) {
        EntityManager entityManager = JpaConnection.getEntityManager();
        try {
            return entityManager.find(Curso.class, id);
        } finally {
            entityManager.close();
        }
    }

    // Lista todos os cursos.
    public List<Curso> findAll() {
        EntityManager entityManager = JpaConnection.getEntityManager();
        try {
            return entityManager.createQuery("from Curso", Curso.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    // Atualiza os dados de um curso.
    public Curso update(Curso curso) {
        return JpaConnection.
                executeInTransaction(entityManager ->
                        entityManager.merge(curso));
    }

    // Remove um curso pelo ID e informa se a remoção ocorreu.
    public boolean deleteById(long id) {
        return JpaConnection.executeInTransaction(entityManager -> {
            Curso curso = entityManager.find(Curso.class, id);
            if (curso == null) {
                return false;
            }
            entityManager.remove(curso);
            return true;
        });
    }

    // Remove todos os cursos e retorna a quantidade apagada.
    public int deleteAll() {
        return JpaConnection.executeInTransaction(entityManager ->
                entityManager.createQuery("delete from Curso").executeUpdate()
        );
    }

    // Retorna o próximo ID sequencial.
    public long nextId() {
        return ID_SEQ.incrementAndGet();
    }
}
