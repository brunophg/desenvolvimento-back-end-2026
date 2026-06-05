package org.api.item;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dao.ItemDao;
import org.dto.item.ItemRequest;
import org.dto.item.ItemResponse;
import org.model.Item;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/itens")
@Tag(name = "Itens", description = "Operacoes de cadastro e consulta de itens")
public class ItemController {
    private final ItemDao itemDao;

    public ItemController(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @GetMapping
    @Operation(summary = "Listar itens", description = "Retorna todos os itens cadastrados.")
    public List<ItemResponse> listarTodos() {
        return itemDao.findAll()
                .stream()
                .map(ItemResponse::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar item por ID", description = "Retorna um item especifico pelo identificador.")
    public ItemResponse buscarPorId(@PathVariable long id) {
        Item item = itemDao.findById(id);
        if (item == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item nao encontrado");
        }
        return ItemResponse.fromEntity(item);
    }

    @GetMapping("/next-id")
    @Operation(summary = "Obter proximo ID de item", description = "Retorna o proximo identificador sequencial disponivel para item.")
    public long proximoId() {
        return itemDao.nextId();
    }

    @PostMapping
    @Operation(summary = "Criar item", description = "Cria um novo item. Se o ID nao for informado, o sistema gera o proximo ID.")
    public ResponseEntity<ItemResponse> criar(@RequestBody ItemRequest request) {
        Item item = request.toEntity();
        if (request.id() == null) {
            item.setId(itemDao.nextId());
        }
        Item itemCriado = itemDao.create(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(ItemResponse.fromEntity(itemCriado));
    }

    @PostMapping("/update")
    @Operation(summary = "Atualizar item", description = "Atualiza um item existente a partir do ID informado no corpo da requisicao.")
    public ItemResponse atualizar(@RequestBody ItemRequest request) {
        if (request.id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id e obrigatorio para atualizacao");
        }
        return ItemResponse.fromEntity(itemDao.update(request.toEntity()));
    }

    @PostMapping("/{id}/delete")
    @Operation(summary = "Remover item por ID", description = "Remove um item existente pelo identificador.")
    public ResponseEntity<Void> removerPorId(@PathVariable long id) {
        if (!itemDao.deleteById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item nao encontrado");
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/delete-all")
    @Operation(summary = "Remover todos os itens", description = "Exclui todos os itens cadastrados e retorna a quantidade removida.")
    public int removerTodos() {
        return 0;
    }
}
