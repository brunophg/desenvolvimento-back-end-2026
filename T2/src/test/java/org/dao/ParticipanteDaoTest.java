package org.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.model.Participante;
import org.support.TestDatabaseSupport;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParticipanteDaoTest {

    private final ParticipanteDao dao = new ParticipanteDao();

    @BeforeEach
    void limpar() {
        TestDatabaseSupport.clearDatabase();
    }


    private Participante criarParticipante() {
        Participante p = new Participante();
        p.setId(dao.nextId());
        p.setCodigo(100);
        p.setRazaoSocial("Participante Teste");
        p.setCnpj("00.000.000/0001-00");
        return p;
    }

    @Test
    void deveCriarParticipante() {
        Participante p = criarParticipante();

        dao.create(p);

        assertNotNull(dao.findById(p.getId()));
    }

    @Test
    void deveBuscarPorId() {
        Participante p = criarParticipante();

        dao.create(p);

        Participante encontrado = dao.findById(p.getId());

        assertEquals(p.getRazaoSocial(), encontrado.getRazaoSocial());
    }

    @Test
    void deveListar() {
        Participante p1 = criarParticipante();
        dao.create(p1);

        Participante p2 = criarParticipante();
        p2.setId(dao.nextId());
        p2.setRazaoSocial("Participante 2");
        dao.create(p2);

        List<Participante> lista = dao.findAll();

        assertTrue(lista.size() >= 2);
    }

    @Test
    void deveAtualizar() {
        Participante p = criarParticipante();

        dao.create(p);

        p.setRazaoSocial("Participante Atualizado");
        dao.update(p);

        Participante atualizado = dao.findById(p.getId());

        assertEquals("Participante Atualizado", atualizado.getRazaoSocial());
    }

    @Test
    void deveRemoverPorId() {
        Participante p = criarParticipante();

        dao.create(p);

        assertTrue(dao.deleteById(p.getId()));
        assertNull(dao.findById(p.getId()));
    }

    @Test
    void deveRemoverTodos() {
        Participante p1 = criarParticipante();
        dao.create(p1);

        Participante p2 = criarParticipante();
        p2.setId(dao.nextId());
        dao.create(p2);

        int removidos = dao.deleteAll();

        assertTrue(removidos >= 2);
        assertTrue(dao.findAll().isEmpty());
    }

    @Test
    void naoDeveRemoverQuandoIdNaoExiste() {
        assertFalse(dao.deleteById(9999L));
    }
}
