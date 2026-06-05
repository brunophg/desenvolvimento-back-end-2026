package org.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.model.Empresa;
import org.support.TestDatabaseSupport;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EmpresaDaoTest {

    private final EmpresaDao dao = new EmpresaDao();

    // Limpa registros antes de cada teste.
    @BeforeEach
    void limpar() {
        TestDatabaseSupport.clearDatabase();
    }

    @Test
    void deveCriarEmpresa() {
        Empresa empresa = new Empresa();
        empresa.setId(1);
        empresa.setCodigo(101);
        empresa.setRazaoSocial("Empresa Teste JF");
        empresa.setEndereco("Rua x");
        empresa.setCnpj("23.333.0001/35");
        Empresa criada = dao.create(empresa);
        assertNotNull(dao.findById(criada.getId()));
    }

    @Test
    void deveBuscarPorId() {
        Empresa empresa = new Empresa();
        empresa.setId(dao.nextId());
        empresa.setCodigo(102);
        empresa.setRazaoSocial("Empresa Teste JF");
        empresa.setEndereco("Rua x");
        empresa.setCnpj("23.444.0001/35");
        dao.create(empresa);
        Empresa encontrada = dao.findById(empresa.getId());
        assertEquals(empresa.getRazaoSocial(), encontrada.getRazaoSocial());
    }
    @Test
    void deveListar() {
        Empresa empresa = new Empresa();
        empresa.setId(dao.nextId());
        empresa.setCodigo(102);
        empresa.setRazaoSocial("Empresa 3");
        empresa.setEndereco("Rua Y");
        empresa.setCnpj("53.353.0001/38");
        dao.create(empresa);
        Empresa empresa2 = new Empresa();
        empresa2.setId(dao.nextId());
        empresa2.setCodigo(102);
        empresa2.setRazaoSocial("Empresa 4");
        empresa2.setEndereco("Rua Z");
        empresa2.setCnpj("44.353.0001/84");
        dao.create(empresa2);
        List<Empresa> list = dao.findAll();
        assertTrue(list.size() >= 2);

    }

    @Test
    void deveAtualizar() {
        Empresa empresa = new Empresa();
        empresa.setId(dao.nextId());
        empresa.setId(dao.nextId());
        empresa.setCodigo(120);
        empresa.setRazaoSocial("Empresa 6");
        empresa.setEndereco("Rua W");
        empresa.setCnpj("73.553.0001/38");
        dao.create(empresa);
        empresa.setEndereco(empresa.getEndereco() + " Nº 1578");
        dao.update(empresa);
        Empresa atualizada = dao.findById(empresa.getId());
        assertNotNull(atualizada, "A empresa deveria existir no banco");
        assertEquals("Rua W Nº 1578", atualizada.getEndereco());
    }

    @Test
    void deveRemoverPorId() {
        Empresa e1 = new Empresa();
        e1.setId(dao.nextId());
        e1.setCodigo(130);
        e1.setEndereco("Avenida Independencia 2140");
        e1.setCnpj("01.122.0002/54");
        e1.setRazaoSocial("Empresa 7");
        dao.create(e1);
        assertTrue(dao.deleteById(e1.getId()));
        assertTrue(dao.findAll().isEmpty());
    }
    @Test
    void deveRemoverTodos() {
        Empresa empresa = new Empresa();
        empresa.setId(dao.nextId());
        empresa.setId(dao.nextId());
        empresa.setCodigo(120);
        empresa.setRazaoSocial("Empresa 8");
        empresa.setEndereco("Rua W");
        empresa.setCnpj("73.553.0001/38");
        dao.create(empresa);
        Empresa empresa2 = new Empresa();
        empresa2.setId(dao.nextId());
        empresa2.setCodigo(234);
        empresa2.setRazaoSocial("Empresa 9");
        empresa2.setEndereco("Rua V");
        empresa2.setCnpj("55.334.0001/47");
        dao.create(empresa2);
        int removidos = dao.deleteAll();
        assertTrue(removidos >= 2);
        assertTrue(dao.findAll().isEmpty());
    }
}