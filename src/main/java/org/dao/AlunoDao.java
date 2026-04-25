package org.dao;

import jakarta.persistence.EntityManager;
import org.config.JpaConnection;
import org.Aluno;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

// DAO responsável pelas operações CRUD da entidade Aluno.
@Repository
public class AlunoDao {
    private static final AtomicLong ID_SEQ = new AtomicLong(4000L);

    // Salva um novo Aluno.
    public Aluno create(Aluno aluno) {
        return JpaConnection.executeInTransaction(entityManager -> {
            entityManager.persist(aluno);
            return aluno;
        });
    }

    // Busca um Aluno pelo ID.
    public Aluno findById(long id) {
        EntityManager entityManager = JpaConnection.getEntityManager();
        try {
            return entityManager.find(Aluno.class, id);
        } finally {
            entityManager.close();
        }
    }

    // Lista todos os Alunos.
    public List<Aluno> findAll() {
        EntityManager entityManager = JpaConnection.getEntityManager();
        try {
            return entityManager.createQuery("from Aluno", Aluno.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    // Atualiza os dados de um Alunos.
    public Aluno update(Aluno aluno) {
        return JpaConnection.executeInTransaction(entityManager -> entityManager.merge(aluno));
    }

    // Remove um Aluno pelo ID e informa se a remoção ocorreu.
    public boolean deleteById(long id) {
        return JpaConnection.executeInTransaction(entityManager -> {
            Aluno aluno = entityManager.find(Aluno.class, id);
            if (aluno == null) {
                return false;
            }
            entityManager.remove(aluno);
            return true;
        });
    }

    // Remove todos os Alunos e retorna a quantidade apagada.
    public int deleteAll() {
        return JpaConnection.executeInTransaction(entityManager ->
                entityManager.createQuery("delete from Aluno").executeUpdate()
        );
    }

    // Retorna o próximo ID sequencial.
    public long nextId() {
        return ID_SEQ.incrementAndGet();
    }
}
