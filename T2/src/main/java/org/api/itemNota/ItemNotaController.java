package org.api.itemNota;


import org.dao.ItemNotaDao;
import org.dao.NotaDao;
import org.dao.ProdutoDao;
import org.model.ItemNota;
import org.model.Nota;
import org.model.Produto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/itens")
public class ItemNotaController {

    private final ItemNotaDao itemNotaDao;
    private final NotaDao notaDao;
    private final ProdutoDao produtoDao;

    public ItemNotaController(ItemNotaDao itemNotaDao, NotaDao notaDao, ProdutoDao produtoDao) {
        this.itemNotaDao = itemNotaDao;
        this.notaDao = notaDao;
        this.produtoDao = produtoDao;
    }

    @GetMapping
    public List<ItemNotaResponse> listarTodos() {
        return itemNotaDao.findAll()
                .stream()
                .map(ItemNotaResponse::fromEntity) // Pega cada ItemNota que passar por aqui e joga dentro do fromEntity"
                .toList();
    }
    @GetMapping("/{id}")
    public ItemNotaResponse buscarPorId(@PathVariable long id) {
        ItemNota item = itemNotaDao.findById(id);
        if (item == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item da nota não encontrado");
        }
        return ItemNotaResponse.fromEntity(item);
    }
    @GetMapping("/next-id")
    public long proximoId()  {
        return itemNotaDao.nextId();
    }
    @PostMapping
    public ResponseEntity<ItemNotaResponse> criar(@RequestBody ItemNotaRequest request) {
        ItemNota item = request.toEntity();

        Nota nota = notaDao.findById(request.idNota());
        Produto prod = produtoDao.findById(request.idProduto());

        if (nota == null || prod == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID da Nota ou do Produto inválido!");
        }
        item.setNota(nota);
        item.setProduto(prod);

        if(request.id() == null) {
            item.setId(itemNotaDao.nextId());
        }
        ItemNota itemCriado = itemNotaDao.create(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(ItemNotaResponse.fromEntity(itemCriado));
    }

    @PostMapping("/update")
    public ItemNotaResponse atualizar(@RequestBody ItemNotaRequest request) {
        if (request.id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id obrigatorio para atualização");
        }
        ItemNota item = request.toEntity();
        Nota nota = notaDao.findById(request.idNota());
        Produto prod = produtoDao.findById(request.idProduto());

        if (nota == null || prod == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID da Nota ou do Produto inválido!");
        }
        item.setNota(nota);
        item.setProduto(prod);

        return ItemNotaResponse.fromEntity(itemNotaDao.update(item));
    }
    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> removerPorId(@PathVariable long id) {
        if (!itemNotaDao.deleteById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item da nota não encontrado");
        }
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/delete-all")
    public int removerTodos() {
        return itemNotaDao.deleteAll();
    }

}