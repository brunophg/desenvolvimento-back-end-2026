package org.api.produto;

import org.dao.ProdutoDao;
import org.model.Produto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoDao produtoDao;

    public ProdutoController(ProdutoDao produtoDao) {
        this.produtoDao = produtoDao;
    }

    @GetMapping
    public List<ProdutoResponse> listarTodos() {
        return produtoDao.findAll()
                .stream()
                .map(ProdutoResponse::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscarPorId(@PathVariable long id) {
        Produto produto = produtoDao.findById(id);
        if (produto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
        }
        return ProdutoResponse.fromEntity(produto);
    }

    @GetMapping("/next-id")
    public Long proximoId() {
        return produtoDao.nextId();
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(@RequestBody ProdutoRequest request) {
        Produto produto = request.toEntity();

        if (request.id() == null) {
            produto.setId(produtoDao.nextId());
        }

        Produto produtoCriado = produtoDao.create(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProdutoResponse.fromEntity(produtoCriado));
    }

    @PostMapping("/update")
    public ProdutoResponse atualizar(@RequestBody ProdutoRequest request) {
        if (request.id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id obrigatorio para atualização");
        }
        Produto produto = request.toEntity();
        return ProdutoResponse.fromEntity(produtoDao.update(produto));
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> removerPorId(@PathVariable long id) {
        if (!produtoDao.deleteById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
        }
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/delete-all")
    public int removerTodos() {
        return produtoDao.deleteAll();
    }

}