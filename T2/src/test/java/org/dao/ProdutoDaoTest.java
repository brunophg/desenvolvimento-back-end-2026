package org.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.model.Produto;
import org.support.TestDatabaseSupport;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProdutoDaoTest {

    private final ProdutoDao dao = new ProdutoDao();

    @BeforeEach
    void limpar() {
        TestDatabaseSupport.clearDatabase();
    }

    private Produto criarProduto() {
        Produto produto = new Produto();
        produto.setId(dao.nextId());
        produto.setCodigo("P001");
        produto.setDescricao("Produto Teste");
        return produto;
    }

    @Test
    void deveCriarProduto() {
        Produto produto = criarProduto();

        dao.create(produto);

        Produto encontrado = dao.findById(produto.getId());
        assertNotNull(encontrado);
    }

    @Test
    void deveBuscarPorId() {
        Produto produto = criarProduto();

        dao.create(produto);

        Produto encontrado = dao.findById(produto.getId());

        assertEquals(produto.getCodigo(), encontrado.getCodigo());
        assertEquals(produto.getDescricao(), encontrado.getDescricao());
    }

    @Test
    void deveListarProdutos() {
        Produto p1 = criarProduto();
        dao.create(p1);

        Produto p2 = criarProduto();
        p2.setId(dao.nextId());
        p2.setCodigo("P002");
        p2.setDescricao("Produto 2");
        dao.create(p2);

        List<Produto> lista = dao.findAll();

        assertNotNull(lista);
        assertTrue(lista.size() >= 2);
    }

    @Test
    void deveAtualizarProduto() {
        Produto produto = criarProduto();

        dao.create(produto);

        produto.setDescricao("Produto Atualizado");
        dao.update(produto);

        Produto atualizado = dao.findById(produto.getId());

        assertEquals("Produto Atualizado", atualizado.getDescricao());
    }

    @Test
    void deveRemoverPorId() {
        Produto produto = criarProduto();

        dao.create(produto);

        boolean removido = dao.deleteById(produto.getId());

        assertTrue(removido);
        assertNull(dao.findById(produto.getId()));
    }

    @Test
    void deveRemoverTodos() {
        Produto p1 = criarProduto();
        dao.create(p1);

        Produto p2 = criarProduto();
        p2.setId(dao.nextId());
        dao.create(p2);

        int removidos = dao.deleteAll();

        assertTrue(removidos >= 2);
        assertTrue(dao.findAll().isEmpty());
    }

    @Test
    void naoDeveRemoverQuandoIdNaoExiste() {
        boolean removido = dao.deleteById(9999L);

        assertFalse(removido);
    }
}
