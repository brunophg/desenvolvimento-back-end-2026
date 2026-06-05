package org.dao;

import org.model.Jogo;
import org.model.Jogador;
import org.model.Partida;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import({PartidaDao.class, JogadorDao.class, JogoDao.class})
class PartidaDaoTest {
    @Autowired
    private PartidaDao dao;

    @Autowired
    private JogadorDao jogadorDao;

    @Autowired
    private JogoDao jogoDao;

    private Jogador jogadorFixo;
    private Jogo jogoFixo;

    @BeforeEach
    void criarDependentes() {
        jogadorFixo = new Jogador();
        jogadorFixo.setId(jogadorDao.nextId());
        jogadorFixo.setNome("Bruno");
        jogadorFixo.setNickname("bruno");
        jogadorFixo.setEmail("bruno@teste.com");
        jogadorDao.create(jogadorFixo);

        jogoFixo = new Jogo();
        jogoFixo.setId(jogoDao.nextId());
        jogoFixo.setNome("FIFA");
        jogoFixo.setGenero("Esporte");
        jogoDao.create(jogoFixo);
    }

    @Test
    void deveCriarPartida() {
        Partida partida = new Partida();
        partida.setId(dao.nextId());
        partida.setPontuacao(1500);
        partida.setJogador(jogadorFixo);
        partida.setJogo(jogoFixo);
        Partida criado = dao.create(partida);
        assertNotNull(dao.findById(criado.getId()));
    }

    @Test
    void deveBuscarPorId() {
        Partida partida = new Partida();
        partida.setId(dao.nextId());
        partida.setPontuacao(1500);
        partida.setJogador(jogadorFixo);
        partida.setJogo(jogoFixo);
        dao.create(partida);
        Partida encontrado = dao.findById(partida.getId());
        assertNotNull(encontrado);
        assertEquals(partida.getPontuacao(), encontrado.getPontuacao());
    }

    @Test
    void deveListar() {
        Partida p1 = new Partida();
        p1.setId(dao.nextId());
        p1.setPontuacao(1500);
        p1.setJogador(jogadorFixo);
        p1.setJogo(jogoFixo);
        dao.create(p1);

        Partida p2 = new Partida();
        p2.setId(dao.nextId());
        p2.setPontuacao(2000);
        p2.setJogador(jogadorFixo);
        p2.setJogo(jogoFixo);
        dao.create(p2);

        List<Partida> lista = dao.findAll();
        assertTrue(lista.size() >= 2);
    }

    @Test
    void deveAtualizar() {
        Partida partida = new Partida();
        partida.setId(dao.nextId());
        partida.setPontuacao(1500);
        partida.setJogador(jogadorFixo);
        partida.setJogo(jogoFixo);
        dao.create(partida);

        partida.setPontuacao(3000);
        dao.update(partida);
        Partida atualizado = dao.findById(partida.getId());
        assertEquals(3000, atualizado.getPontuacao());
    }

    @Test
    void deveRemoverPorId() {
        Partida partida = new Partida();
        partida.setId(dao.nextId());
        partida.setPontuacao(1500);
        partida.setJogador(jogadorFixo);
        partida.setJogo(jogoFixo);
        dao.create(partida);

        assertTrue(dao.deleteById(partida.getId()));
        assertNull(dao.findById(partida.getId()));
    }

    @Test
    void naoDeveRemoverInexistente() {
        assertFalse(dao.deleteById(-1L));
    }
}