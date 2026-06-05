package org.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.model.Jogo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

// DAO responsável pelas operações CRUD da entidade Jogo.
@Repository
public class JogoDao {

    private static final AtomicLong ID_SEQ = new AtomicLong(4000L);
    @PersistenceContext
    private EntityManager entityManager;

    // Cria um novo jogo
    @Transactional
    public Jogo create(Jogo jogo) {
        entityManager.persist(jogo);
        return jogo;
    }

    // Busca um Jogo pelo ID.
    public Jogo findById(long id) {
        return entityManager.find(Jogo.class, id);
    }

    // Lista todos os Jogos.
    public List<Jogo> findAll() {
        return entityManager.createQuery("from Jogo", Jogo.class).getResultList();
    }

    // Atualiza os dados de um Jogos.
    @Transactional
    public Jogo update(Jogo Jogo) {
        return entityManager.merge(Jogo);
    }

    // Remove um Jogo pelo ID e informa se a remoção ocorreu.
    @Transactional
    public boolean deleteById(long id) {
        Jogo Jogo = entityManager.find(Jogo.class, id);
        if (Jogo == null) {
            return false;
        }
        entityManager.remove(Jogo);
        return true;
    }

    // Retorna o próximo ID sequencial.
    public long nextId() {
        return ID_SEQ.incrementAndGet();
    }
}

