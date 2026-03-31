package org.dao;

import jakarta.persistence.EntityManager;
import org.config.JpaConnection;
import org.model.Empresa;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

// DAO responsável pelas operações CRUD da entidade Empresa.
public class EmpresaDao {
    private static final AtomicLong ID_SEQ = new AtomicLong(2000L);

    // Salva uma nova empresa.
    public Empresa create(Empresa empresa) {
        return JpaConnection.executeInTransaction(entityManager -> {
            entityManager.persist(empresa);
            return empresa;
        });
    }

    // Busca uma empresa pelo ID.
    public Empresa findById(long id) {
        EntityManager entityManager = JpaConnection.getEntityManager();
        try {
            return entityManager.find(Empresa.class, id);
        } finally {
            entityManager.close();
        }
    }

    // Lista todos as empresas.
    public List<Empresa> findAll() {
        EntityManager entityManager = JpaConnection.getEntityManager();
        try {
            return entityManager.createQuery("from Empresa", Empresa.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    // Atualiza os dados de uma empresa.
    public Empresa update(Empresa empresa) {
        return JpaConnection.executeInTransaction(entityManager -> entityManager.merge(empresa));
    }

    // Remove uma empresa pelo ID e informa se a remoção ocorreu.
    public boolean deleteById(long id) {
        return JpaConnection.executeInTransaction(entityManager -> {
            Empresa empresa = entityManager.find(Empresa.class, id);
            if (empresa == null) {
                return false;
            }
            entityManager.remove(empresa);
            return true;
        });
    }

    // Remove todos as empresas e retorna a quantidade apagada.
    public int deleteAll() {
        return JpaConnection.executeInTransaction(entityManager ->
                entityManager.createQuery("delete from Empresa").executeUpdate()
        );
    }

    // Retorna o próximo ID sequencial.
    public long nextId() {
        return ID_SEQ.incrementAndGet();
    }
}
