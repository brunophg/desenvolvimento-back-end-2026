package org.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.model.ItemNota;
import org.model.Nota;
import org.model.Produto;
import org.support.TestDatabaseSupport;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemNotaDaoTest {

    private final ItemNotaDao dao = new ItemNotaDao();

    @BeforeEach
    void limpar() {
        TestDatabaseSupport.clearDatabase();
    }

    private ItemNota criarItemCompleto() {
        Produto produto = new Produto();
        produto.setId(1L);

        Nota nota = new Nota();
        nota.setId(1L);

        ItemNota item = new ItemNota();
        item.setId(dao.nextId());
        item.setProduto(produto);
        item.setNota(nota);
        item.setQuantidade(BigDecimal.valueOf(10));
        item.setVrUnitario(BigDecimal.valueOf(50));

        return item;
    }

    @Test
    void deveCriarItemNota() {
        ItemNota item = criarItemCompleto();

        dao.create(item);

        assertNotNull(dao.findById(item.getId()));
    }

    @Test
    void deveBuscarPorId() {
        ItemNota item = criarItemCompleto();

        dao.create(item);

        ItemNota encontrado = dao.findById(item.getId());

        assertEquals(item.getQuantidade(), encontrado.getQuantidade());
    }

    @Test
    void deveListar() {
        ItemNota item1 = criarItemCompleto();
        dao.create(item1);

        ItemNota item2 = criarItemCompleto();
        item2.setId(dao.nextId());
        dao.create(item2);

        List<ItemNota> lista = dao.findAll();

        assertTrue(lista.size() >= 2);
    }

    @Test
    void deveAtualizar() {
        ItemNota item = criarItemCompleto();

        dao.create(item);

        item.setQuantidade(BigDecimal.valueOf(99));
        dao.update(item);

        ItemNota atualizado = dao.findById(item.getId());

        assertEquals(BigDecimal.valueOf(99), atualizado.getQuantidade());
    }

    @Test
    void deveRemoverPorId() {
        ItemNota item = criarItemCompleto();

        dao.create(item);

        assertTrue(dao.deleteById(item.getId()));
        assertNull(dao.findById(item.getId()));
    }

    @Test
    void deveRemoverTodos() {
        ItemNota item1 = criarItemCompleto();
        dao.create(item1);

        ItemNota item2 = criarItemCompleto();
        item2.setId(dao.nextId());
        dao.create(item2);

        int removidos = dao.deleteAll();

        assertTrue(removidos >= 2);
        assertTrue(dao.findAll().isEmpty());
    }
}
