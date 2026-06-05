package org.dao;

import jakarta.persistence.EntityManager;
import org.config.JpaConnection;
import org.model.Produto;


import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ProdutoDao {
    private static final AtomicLong ID_SEQ = new AtomicLong(2000L);

    // Salva uma nova.
    public Produto create(Produto produto) {
        return JpaConnection.executeInTransaction(entityManager -> {
            entityManager.persist(produto);
            return produto;
        });
    }

    // Busca pelo ID.
    public Produto findById(long id) {
        EntityManager entityManager = JpaConnection.getEntityManager();
        try {
            return entityManager.find(Produto.class, id);
        } finally {
            entityManager.close();
        }
    }

    // Lista todos.
    public List<Produto> findAll() {
        EntityManager entityManager = JpaConnection.getEntityManager();
        try {
            return entityManager.createQuery("from Curso", Produto.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    // Atualiza os dados.
    public Produto update(Produto produto) {
        return JpaConnection.executeInTransaction(entityManager -> entityManager.merge(produto));
    }

    // Remove pelo ID e informa se a remoção ocorreu.
    public boolean deleteById(long id) {
        return JpaConnection.executeInTransaction(entityManager -> {
            Produto produto = entityManager.find(Produto.class, id);
            if (produto == null) {
                return false;
            }
            entityManager.remove(produto);
            return true;
        });
    }

    // Remove todos e retorna a quantidade apagada.
    public int deleteAll() {
        return JpaConnection.executeInTransaction(entityManager ->
                entityManager.createQuery("delete from Produto").executeUpdate()
        );
    }

    // Retorna o próximo ID sequencial.
    public long nextId() {
        return ID_SEQ.incrementAndGet();
    }
}
