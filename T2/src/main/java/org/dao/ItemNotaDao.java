package org.dao;

import jakarta.persistence.EntityManager;
import org.config.JpaConnection;
import org.model.ItemNota;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ItemNotaDao {
    private static final AtomicLong ID_SEQ = new AtomicLong(2000L);

    // Salva um Novo Item Nota.
    public ItemNota create(ItemNota itemNota) {
        return JpaConnection.executeInTransaction(entityManager -> {
            entityManager.persist(itemNota);
            return itemNota;
        });
    }

    // Busca uma ItemNota pelo ID.
    public ItemNota findById(long id) {
        EntityManager entityManager = JpaConnection.getEntityManager();
        try {
            return entityManager.find(ItemNota.class, id);
        } finally {
            entityManager.close();
        }
    }

    // Lista todos os ItensNota.
    public List<ItemNota> findAll() {
        EntityManager entityManager = JpaConnection.getEntityManager();
        try {
            return entityManager.createQuery("from ItemNota", ItemNota.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    // Atualiza os dados de uma itemnota.
    public ItemNota update(ItemNota itemNota) {
        return JpaConnection.executeInTransaction(entityManager -> entityManager.merge(itemNota));
    }

    // Remove pelo ID e informa se a remoção ocorreu.
    public boolean deleteById(long id) {
        return JpaConnection.executeInTransaction(entityManager -> {
            ItemNota itemNota = entityManager.find(ItemNota.class, id);
            if (itemNota == null) {
                return false;
            }
            entityManager.remove(itemNota);
            return true;
        });
    }

    // Remove todos e retorna a quantidade apagada.
    public int deleteAll() {
        return JpaConnection.executeInTransaction(entityManager ->
                entityManager.createQuery("delete from ItemNota").executeUpdate()
        );
    }

    // Retorna o próximo ID sequencial.
    public long nextId() {
        return ID_SEQ.incrementAndGet();
    }
}
