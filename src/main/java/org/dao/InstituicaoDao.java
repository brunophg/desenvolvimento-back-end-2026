package org.dao;

import jakarta.persistence.EntityManager;
import org.config.JpaConnection;
import org.model.Instituicao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

// DAO responsável pelas operações CRUD da entidade Instituicao.
@Repository
public class InstituicaoDao {

    // Gerador simples de IDs para cenários de teste/uso manual.
    private static final AtomicLong ID_SEQ = new AtomicLong(1000L);

    // Salva uma nova instituição.
    public Instituicao create(Instituicao instituicao) {
        return JpaConnection.executeInTransaction(entityManager -> {
            entityManager.persist(instituicao);
            return instituicao;
        });
    }

    // Busca uma instituição pelo ID.
    public Instituicao findById(long id) {
        EntityManager entityManager = JpaConnection.getEntityManager();
        try {
            return entityManager.find(Instituicao.class, id);
        } finally {
            entityManager.close();
        }
    }

    // Lista todas as instituições.
    public List<Instituicao> findAll() {
        EntityManager entityManager = JpaConnection.getEntityManager();
        try {
            return entityManager.createQuery("from Instituicao", Instituicao.class)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    // Atualiza os dados de uma instituição.
    public Instituicao update(Instituicao instituicao) {
        return JpaConnection.executeInTransaction(entityManager -> entityManager.merge(instituicao));
    }

    // Remove uma instituição pelo ID e informa se a remoção ocorreu.
    public boolean deleteById(long id) {
        return JpaConnection.executeInTransaction(entityManager -> {
            Instituicao instituicao = entityManager.find(Instituicao.class, id);
            if (instituicao == null) {
                return false;
            }
            entityManager.remove(instituicao);
            return true;
        });
    }

    // Remove todas as instituições e retorna a quantidade de registros apagados.
    public int deleteAll() {
        return JpaConnection.executeInTransaction(entityManager ->
                entityManager.createQuery("delete from Instituicao").executeUpdate()
        );
    }

    // Retorna o próximo ID sequencial.
    public long nextId() {
        return ID_SEQ.incrementAndGet();
    }
}
