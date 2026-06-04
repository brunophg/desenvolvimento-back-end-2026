package org.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.model.Jogador;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

// DAO responsável pelas operações CRUD da entidade Jogador.
@Repository
public class JogadorDao {
    private static final AtomicLong ID_SEQ = new AtomicLong(4000L);
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Jogador create(Jogador jogador) {
        entityManager.persist(jogador);
        return jogador;
    }

    public Jogador findById(long id) {
        return entityManager.find(Jogador.class, id);
    }

    public List<Jogador> findAll() {
        return entityManager.createQuery("from Jogador", Jogador.class).getResultList();
    }

    @Transactional
    public Jogador update(Jogador jogador) {
        return entityManager.merge(jogador);
    }

    @Transactional
    public boolean deleteById(long id) {
        Jogador jogador = entityManager.find(Jogador.class, id);
        if (jogador == null) {
            return false;
        }
        entityManager.remove(jogador);
        return true;
    }

    @Transactional
    public int deleteAll() {
        return 0;
    }

    public long nextId() {
        return ID_SEQ.incrementAndGet();
    }
}
