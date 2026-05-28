package org.dao;

import org.model.Inventario;
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
@Import(InventarioDao.class)
class InventarioDaoTest {
    @Autowired
    private InventarioDao dao;

    @Test
    void deveCriarInventario() {
        Inventario inventario = new Inventario();
        inventario.setId(dao.nextId());
        inventario.setQuantidade(5);
        Inventario criado = dao.create(inventario);
        assertNotNull(dao.findById(criado.getId()));
    }

    @Test
    void deveBuscarPorId() {
        Inventario inventario = new Inventario();
        inventario.setId(dao.nextId());
        inventario.setQuantidade(5);
        dao.create(inventario);
        Inventario encontrado = dao.findById(inventario.getId());
        assertNotNull(encontrado);
        assertEquals(inventario.getQuantidade(), encontrado.getQuantidade());
    }

    @Test
    void deveListar() {
        Inventario i1 = new Inventario();
        i1.setId(dao.nextId());
        i1.setQuantidade(5);
        dao.create(i1);
        Inventario i2 = new Inventario();
        i2.setId(dao.nextId());
        i2.setQuantidade(10);
        dao.create(i2);
        List<Inventario> lista = dao.findAll();
        assertTrue(lista.size() >= 2);
    }

    @Test
    void deveAtualizar() {
        Inventario inventario = new Inventario();
        inventario.setId(dao.nextId());
        inventario.setQuantidade(5);
        dao.create(inventario);
        inventario.setQuantidade(15);
        dao.update(inventario);
        Inventario atualizado = dao.findById(inventario.getId());
        assertEquals(15, atualizado.getQuantidade());
    }

    @Test
    void deveRemoverPorId() {
        Inventario inventario = new Inventario();
        inventario.setId(dao.nextId());
        inventario.setQuantidade(5);
        dao.create(inventario);
        assertTrue(dao.deleteById(inventario.getId()));
        assertNull(dao.findById(inventario.getId()));
    }

    @Test
    void naoDeveRemoverInexistente() {
        assertFalse(dao.deleteById(-1L));
    }
}