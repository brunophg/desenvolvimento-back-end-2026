package org.dao;

import org.model.Jogador;
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
@Import(JogadorDao.class)
class JogadorDaoTest {
    @Autowired
    private JogadorDao dao;

    @Test
    void deveCriarJogador() {
        Jogador jogador = new Jogador();
        jogador.setId(dao.nextId());
        jogador.setNome(textoTeste());
        jogador.setNickname(textoTeste());
        jogador.setEmail(textoTeste() + "@email.com");
        Jogador criado = dao.create(jogador);
        assertNotNull(dao.findById(criado.getId()));
    }

    @Test
    void deveBuscarPorId() {
        Jogador jogador = new Jogador();
        jogador.setId(dao.nextId());
        jogador.setNome(textoTeste());
        jogador.setNickname(textoTeste());
        jogador.setEmail(textoTeste() + "@email.com");
        dao.create(jogador);
        Jogador encontrado = dao.findById(jogador.getId());
        assertNotNull(encontrado);
        assertEquals(jogador.getNome(), encontrado.getNome());
    }

    @Test
    void deveListar() {
        Jogador j1 = new Jogador();
        j1.setId(dao.nextId());
        j1.setNome(textoTeste());
        j1.setNickname(textoTeste());
        j1.setEmail(textoTeste() + "@email.com");
        dao.create(j1);
        Jogador j2 = new Jogador();
        j2.setId(dao.nextId());
        j2.setNome(textoTeste());
        j2.setNickname(textoTeste());
        j2.setEmail(textoTeste() + "@email.com");
        dao.create(j2);
        List<Jogador> lista = dao.findAll();
        assertTrue(lista.size() >= 2);
    }

    @Test
    void deveAtualizar() {
        Jogador jogador = new Jogador();
        jogador.setId(dao.nextId());
        jogador.setNome(textoTeste());
        jogador.setNickname(textoTeste());
        jogador.setEmail(textoTeste() + "@email.com");
        dao.create(jogador);
        jogador.setNome(jogador.getNome() + "-UPD");
        dao.update(jogador);
        Jogador atualizado = dao.findById(jogador.getId());
        assertEquals(jogador.getNome(), atualizado.getNome());
    }

    @Test
    void deveRemoverPorId() {
        Jogador jogador = new Jogador();
        jogador.setId(dao.nextId());
        jogador.setNome(textoTeste());
        jogador.setNickname(textoTeste());
        jogador.setEmail(textoTeste() + "@email.com");
        dao.create(jogador);
        assertTrue(dao.deleteById(jogador.getId()));
        assertNull(dao.findById(jogador.getId()));
    }

    @Test
    void naoDeveRemoverInexistente() {
        assertFalse(dao.deleteById(-1L));
    }

    private String textoTeste() {
        return "TEST-" + UUID.randomUUID().toString().substring(0, 8);
    }
}