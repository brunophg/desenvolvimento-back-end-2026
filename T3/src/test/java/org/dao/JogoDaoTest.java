package org.dao;

import org.model.Jogo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(JogoDao.class)
class JogoDaoTest {
    @Autowired
    private JogoDao dao;

    @Test
    void deveCriarJogo() {
        Jogo jogo = new Jogo();
        jogo.setId(dao.nextId());
        jogo.setNome(textoTeste());
        jogo.setGenero("Esportes");
        Jogo criado = dao.create(jogo);
        assertNotNull(dao.findById(criado.getId()));
    }

    @Test
    void deveBuscarPorId() {
        Jogo jogo = new Jogo();
        jogo.setId(dao.nextId());
        jogo.setNome(textoTeste());
        jogo.setGenero("Esportes");
        dao.create(jogo);
        Jogo encontrado = dao.findById(jogo.getId());
        assertNotNull(encontrado);
        assertEquals(jogo.getNome(), encontrado.getNome());
    }

    @Test
    void deveListar() {
        Jogo j1 = new Jogo();
        j1.setId(dao.nextId());
        j1.setNome(textoTeste());
        j1.setGenero("Esportes");
        dao.create(j1);
        Jogo j2 = new Jogo();
        j2.setId(dao.nextId());
        j2.setNome(textoTeste());
        j2.setGenero("Aventura");
        dao.create(j2);
        List<Jogo> lista = dao.findAll();
        assertTrue(lista.size() >= 2);
    }

    @Test
    void deveAtualizar() {
        Jogo jogo = new Jogo();
        jogo.setId(dao.nextId());
        jogo.setNome(textoTeste());
        jogo.setGenero("Esportes");
        dao.create(jogo);
        jogo.setNome(jogo.getNome() + "-UPD");
        dao.update(jogo);
        Jogo atualizado = dao.findById(jogo.getId());
        assertEquals(jogo.getNome(), atualizado.getNome());
    }

    @Test
    void deveRemoverPorId() {
        Jogo jogo = new Jogo();
        jogo.setId(dao.nextId());
        jogo.setNome(textoTeste());
        jogo.setGenero("Esportes");
        dao.create(jogo);
        assertTrue(dao.deleteById(jogo.getId()));
        assertNull(dao.findById(jogo.getId()));
    }

    @Test
    void naoDeveRemoverInexistente() {
        assertFalse(dao.deleteById(-1L));
    }

    private String textoTeste() {
        return "TEST-" + UUID.randomUUID().toString().substring(0, 8);
    }
}