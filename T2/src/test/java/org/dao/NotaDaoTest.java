package org.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.model.Empresa;
import org.model.Nota;
import org.model.Participante;
import org.support.TestDatabaseSupport;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NotaDaoTest {

    private final NotaDao dao = new NotaDao();
    private final EmpresaDao empresaDao = new EmpresaDao();
    private final ParticipanteDao participanteDao = new ParticipanteDao();

    @BeforeEach
    void limpar() {
        TestDatabaseSupport.clearDatabase();
    }

    private Empresa criarEmpresa() {
        Empresa empresa = new Empresa();
        empresa.setId(empresaDao.nextId());
        empresa.setCodigo(100);
        empresa.setRazaoSocial("Empresa Teste");
        empresa.setEndereco("Rua X");
        empresa.setCnpj("00.000.000/0001-00");

        return empresaDao.create(empresa);
    }

    private Participante criarParticipante() {
        Participante participante = new Participante();
        participante.setId(participanteDao.nextId());
        participante.setRazaoSocial("Participante Teste");

        return participanteDao.create(participante);
    }

    private Nota criarNotaCompleta() {
        Empresa empresa = criarEmpresa();
        Participante participante = criarParticipante();

        Nota nota = new Nota();
        nota.setId(dao.nextId());
        nota.setNumero(123);
        nota.setData(new Date());
        nota.setEmpresa(empresa);
        nota.setParticipante(participante);

        return nota;
    }

    @Test
    void deveCriarNota() {
        Nota nota = criarNotaCompleta();

        dao.create(nota);

        assertNotNull(dao.findById(nota.getId()));
    }

    @Test
    void deveBuscarPorId() {
        Nota nota = criarNotaCompleta();

        dao.create(nota);

        Nota encontrada = dao.findById(nota.getId());

        assertEquals(nota.getNumero(), encontrada.getNumero());
    }

    @Test
    void deveListar() {
        Nota nota1 = criarNotaCompleta();
        dao.create(nota1);

        Nota nota2 = criarNotaCompleta();
        nota2.setId(dao.nextId());
        dao.create(nota2);

        List<Nota> lista = dao.findAll();

        assertTrue(lista.size() >= 2);
    }

    @Test
    void deveAtualizar() {
        Nota nota = criarNotaCompleta();

        dao.create(nota);

        nota.setNumero(999);
        dao.update(nota);

        Nota atualizada = dao.findById(nota.getId());

        assertEquals(999, atualizada.getNumero());
    }

    @Test
    void deveRemoverPorId() {
        Nota nota = criarNotaCompleta();

        dao.create(nota);

        assertTrue(dao.deleteById(nota.getId()));
        assertNull(dao.findById(nota.getId()));
    }

    @Test
    void deveRemoverTodos() {
        Nota nota1 = criarNotaCompleta();
        dao.create(nota1);

        Nota nota2 = criarNotaCompleta();
        nota2.setId(dao.nextId());
        dao.create(nota2);

        int removidos = dao.deleteAll();

        assertTrue(removidos >= 2);
        assertTrue(dao.findAll().isEmpty());
    }
}
