package org.dao;

import jakarta.persistence.EntityManager;
import org.config.JpaConnection;
import org.model.Situacao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

// DAO responsável pelas operações CRUD da entidade Situacao.
@Repository
public class SituacaoDao {
    private static final AtomicLong ID_SEQ = new AtomicLong(5000L);

    // Salva uma nova situação.
    public Situacao create(Situacao situacao) {
        return JpaConnection.executeInTransaction(entityManager -> {
            entityManager.persist(situacao);
            return situacao;
        });
    }

    // Busca uma situação pelo ID.
    public Situacao findById(long id) {
        EntityManager entityManager = JpaConnection.getEntityManager();
        try {
            return entityManager.find(Situacao.class, id);
        } finally {
            entityManager.close();
        }
    }

    // Lista todas as situações.
    public List<Situacao> findAll() {
        EntityManager entityManager = JpaConnection.getEntityManager();
        try {
            return entityManager.createQuery("from Situacao", Situacao.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    // Atualiza os dados de uma situação.
    public Situacao update(Situacao situacao) {
        return JpaConnection.executeInTransaction(entityManager -> entityManager.merge(situacao));
    }

    // Remove uma situação pelo ID e informa se a remoção ocorreu.
    public boolean deleteById(long id) {
        return JpaConnection.executeInTransaction(entityManager -> {
            Situacao situacao = entityManager.find(Situacao.class, id);
            if (situacao == null) {
                return false;
            }
            situacao.getAlunos().forEach(aluno -> aluno.getSituacoes().remove(situacao));
            situacao.getAlunos().clear();
            entityManager.remove(situacao);
            return true;
        });
    }

    // Remove todas as situações e retorna a quantidade apagada.
    public int deleteAll() {
        return JpaConnection.executeInTransaction(entityManager -> {
            entityManager.createNativeQuery("delete from aluno_situacao").executeUpdate();
            return entityManager.createQuery("delete from Situacao").executeUpdate();
        });
    }

    // Retorna o próximo ID sequencial.
    public long nextId() {
        return ID_SEQ.incrementAndGet();
    }
}
