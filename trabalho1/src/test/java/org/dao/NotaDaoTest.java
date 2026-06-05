package org.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.model.Empresa;
import org.model.Nota;
import org.model.Participante;
import org.support.TestDatabaseSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class NotaDaoTest {

    private final NotaDao notaDao = new NotaDao();
    private final ParticipanteDao participanteDao = new ParticipanteDao();
    private final EmpresaDao empresaDao = new EmpresaDao();

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @BeforeEach
    void deveLimpar() {
        TestDatabaseSupport.clearDatabase();
    }

    @Test
    void deveCriarNota() throws ParseException {

        Participante part = criarParticipanteAuxiliar();
        Empresa emp = criarEmpresaAuxiliar();

        Nota n1 = new Nota();
        n1.setId(notaDao.nextId());
        n1.setData(sdf.parse("01/04/2026"));
        n1.setNumero(12343);
        n1.setEmpresa(emp);
        n1.setParticipante(part);
        Nota notaCriada = notaDao.create(n1);
        assertNotNull(notaCriada, "A nota deveria ter sido salva no banco");
        assertEquals(12343, notaCriada.getNumero());
        assertEquals(part.getId(), notaCriada.getParticipante().getId());
    }

    @Test
    void deveBuscarPorId() throws ParseException {
        Participante part = criarParticipanteAuxiliar();
        Empresa emp = criarEmpresaAuxiliar();

        Nota n1 = new Nota();
        n1.setId(notaDao.nextId());
        n1.setData(sdf.parse("01/04/2026"));
        n1.setNumero(4785);
        n1.setParticipante(part);
        n1.setEmpresa(emp);
        Nota notaEncontrada = notaDao.create(n1);
        assertNotNull(notaEncontrada, "A nota deveria ter sido salva no banco");
        assertEquals(4785, notaEncontrada.getNumero());

    }
    @Test
    void deveListar() throws ParseException {
        Participante part = criarParticipanteAuxiliar();
        Empresa emp = criarEmpresaAuxiliar();

        Nota n1 = new Nota();
        n1.setId(notaDao.nextId());
        n1.setData(sdf.parse("02/04/2026"));
        n1.setNumero(2580);
        n1.setParticipante(part);
        n1.setEmpresa(emp);
        notaDao.create(n1);

        Nota n2 = new Nota();
        n2.setId(notaDao.nextId());
        n2.setData(sdf.parse("03/04/2026"));
        n2.setNumero(2590);
        n2.setParticipante(part);
        n2.setEmpresa(emp);
        notaDao.create(n2);

        ArrayList<Nota> notas = (ArrayList<Nota>) notaDao.findAll();
        assertTrue(notas.size() >= 2);

    }
    @Test
    void deveAtualizar() throws ParseException {
        Participante part = criarParticipanteAuxiliar();
        Empresa emp = criarEmpresaAuxiliar();

        Nota n1 = new Nota();
        n1.setId(notaDao.nextId());
        n1.setData(sdf.parse("05/04/2026"));
        n1.setNumero(3000);
        n1.setEmpresa(emp);
        n1.setParticipante(part);
        notaDao.create(n1);

        n1.setNumero(4000);
        n1.setData(sdf.parse("10/04/2026"));

        notaDao.update(n1);

        Nota notaAtualizada = notaDao.findById(n1.getId());
        assertNotNull(notaAtualizada);
        assertEquals(n1.getNumero(), notaAtualizada.getNumero());

    }

    @Test
    void deveRemoverPorId() throws ParseException {
        Participante part = criarParticipanteAuxiliar();
        Empresa emp = criarEmpresaAuxiliar();

        Nota n1 = new Nota();
        n1.setId(notaDao.nextId());
        n1.setData(sdf.parse("15/04/2026"));
        n1.setNumero(5001);
        n1.setEmpresa(emp);
        n1.setParticipante(part);
        notaDao.create(n1);

        boolean removeu = notaDao.deleteById(n1.getId());

        assertTrue(removeu, "O método deveria retornar true ao remover um ID existente");
        assertNull(notaDao.findById(n1.getId()), "A nota não deveria mais existir no banco");
    }

    @Test
    void deveRemoverTodos() throws ParseException {
        Participante part = criarParticipanteAuxiliar();
        Empresa emp = criarEmpresaAuxiliar();

        Nota n1 = new Nota();
        n1.setId(notaDao.nextId());
        n1.setData(sdf.parse("20/04/2026"));
        n1.setNumero(9001);
        n1.setEmpresa(emp);
        n1.setParticipante(part);
        notaDao.create(n1);

        Nota n2 = new Nota();
        n2.setId(notaDao.nextId());
        n2.setData(sdf.parse("21/04/2026"));
        n2.setNumero(9002);
        n2.setEmpresa(emp);
        n2.setParticipante(part);
        notaDao.create(n2);

        int quantidadeRemovida = notaDao.deleteAll();

        assertTrue(quantidadeRemovida >= 2, "Deveriam ter sido removidas pelo menos 2 notas");
        assertTrue(notaDao.findAll().isEmpty(), "A lista de notas deveria estar vazia no banco");
    }

    private Participante criarParticipanteAuxiliar() {
        Participante part = new Participante();
        part.setId(participanteDao.nextId());
        part.setCodigo(221);
        part.setRazaoSocial("Participante Teste S.A.");
        part.setCnpj("22.002.0001/23");
        return participanteDao.create(part);
    }

    private Empresa criarEmpresaAuxiliar() {
        Empresa emp = new Empresa();
        emp.setId(empresaDao.nextId());
        emp.setCodigo(500);
        emp.setRazaoSocial("Empresa Principal LTDA");
        emp.setCnpj("11.222.333/0001-99");
        emp.setEndereco("Rua das Flores, 123");
        return empresaDao.create(emp);
    }

}