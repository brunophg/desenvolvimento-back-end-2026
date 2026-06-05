package org.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.model.Inventario;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InventarioDao {
    private static final AtomicLong ID_SEQ = new AtomicLong(4000L);
    @PersistenceContext
    private EntityManager entityManager;

    // Cria um novo Inventario
    @Transactional
    public Inventario create(Inventario inventario) {
        entityManager.persist(inventario);
        return inventario;
    }

    // Busca um Inventario pelo ID.
    public Inventario findById(long id) {
        return entityManager.find(Inventario.class, id);
    }

    // Lista todos os Inventarios.
    public List<Inventario> findAll() {
        return entityManager.createQuery("from Inventario", Inventario.class).getResultList();
    }

    // Atualiza os dados de um Inventario.
    @Transactional
    public Inventario update(Inventario inventario) {
        return entityManager.merge(inventario);
    }

    // Remove um Inventario pelo ID e informa se a remoção ocorreu.
    @Transactional
    public boolean deleteById(long id) {
        Inventario inventario = entityManager.find(Inventario.class, id);
        if (inventario == null) {
            return false;
        }
        entityManager.remove(inventario);
        return true;
    }

    // Retorna o próximo ID sequencial.
    public long nextId() {
        return ID_SEQ.incrementAndGet();
    }

}