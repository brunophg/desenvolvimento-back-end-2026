package org.dao;

import org.model.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(ItemDao.class)
class ItemDaoTest {
    @Autowired
    private ItemDao dao;

    @Test
    void deveCriarItem() {
        Item item = new Item();
        item.setId(dao.nextId());
        item.setNome(textoTeste());
        item.setTipo("Equipamento");
        item.setValor(BigDecimal.valueOf(100.0));
        Item criado = dao.create(item);
        assertNotNull(dao.findById(criado.getId()));
    }

    @Test
    void deveBuscarPorId() {
        Item item = new Item();
        item.setId(dao.nextId());
        item.setNome(textoTeste());
        item.setTipo("Equipamento");
        item.setValor(BigDecimal.valueOf(100.0));
        dao.create(item);
        Item encontrado = dao.findById(item.getId());
        assertNotNull(encontrado);
        assertEquals(item.getNome(), encontrado.getNome());
    }

    @Test
    void deveListar() {
        Item i1 = new Item();
        i1.setId(dao.nextId());
        i1.setNome(textoTeste());
        i1.setTipo("Equipamento");
        i1.setValor(BigDecimal.valueOf(100.0));
        dao.create(i1);
        Item i2 = new Item();
        i2.setId(dao.nextId());
        i2.setNome(textoTeste());
        i2.setTipo("Cosmetico");
        i2.setValor(BigDecimal.valueOf(200.0));
        dao.create(i2);
        List<Item> lista = dao.findAll();
        assertTrue(lista.size() >= 2);
    }

    @Test
    void deveAtualizar() {
        Item item = new Item();
        item.setId(dao.nextId());
        item.setNome(textoTeste());
        item.setTipo("Equipamento");
        item.setValor(BigDecimal.valueOf(100.0));
        dao.create(item);
        item.setNome(item.getNome() + "-UPD");
        dao.update(item);
        Item atualizado = dao.findById(item.getId());
        assertEquals(item.getNome(), atualizado.getNome());
    }

    @Test
    void deveRemoverPorId() {
        Item item = new Item();
        item.setId(dao.nextId());
        item.setNome(textoTeste());
        item.setTipo("Equipamento");
        item.setValor(BigDecimal.valueOf(100.0));
        dao.create(item);
        assertTrue(dao.deleteById(item.getId()));
        assertNull(dao.findById(item.getId()));
    }

    @Test
    void naoDeveRemoverInexistente() {
        assertFalse(dao.deleteById(-1L));
    }

    private String textoTeste() {
        return "TEST-" + UUID.randomUUID().toString().substring(0, 8);
    }
}