package org.api.inventario;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dao.InventarioDao;
import org.dao.ItemDao;
import org.dao.JogadorDao;
import org.dto.inventario.InventarioRequest;
import org.dto.inventario.InventarioResponse;
import org.model.Inventario;
import org.model.Item;
import org.model.Jogador;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/inventarios")
@Tag(name = "Inventarios", description = "Operacoes de cadastro e consulta de Inventarios")
public class InventarioController {

    private final InventarioDao inventarioDao;
    private final JogadorDao jogadorDao;
    private final ItemDao itemDao;

    public InventarioController(InventarioDao inventarioDao, JogadorDao jogadorDao, ItemDao itemDao) {
        this.inventarioDao = inventarioDao;
        this.jogadorDao = jogadorDao;
        this.itemDao = itemDao;
    }

    @GetMapping
    @Operation(summary = "Listar Inventarios", description = "Retorna todos as Inventarios cadastrados.")
    public List<InventarioResponse> listarTodos() {
        return inventarioDao.findAll()
                .stream()
                .map(InventarioResponse::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar inventario por ID", description = "Retorna uma inventario especifica pelo identificador.")
    public InventarioResponse buscarPorId(@PathVariable long id) {
        Inventario inventario = inventarioDao.findById(id);
        if (inventario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id de inventario invalido");
        }
        return InventarioResponse.fromEntity(inventario);
    }

    @GetMapping("/next-id")
    @Operation(summary = "Obter proximo ID de inventario", description = "Retorna o proximo identificador sequencial disponivel para inventario.")
    public long proximoId() {
        return inventarioDao.nextId();
    }

    @PostMapping
    @Operation(summary = "Criar inventario", description = "Cria uma nova inventario. Se o ID nao for informado, o sistema gera o proximo ID.")
    public ResponseEntity<InventarioResponse> criar(@RequestBody InventarioRequest request) {
        Inventario inventario = request.toEntity();
        if (request.id() == null) {
            inventario.setId(inventarioDao.nextId());
        }
        if (request.idJogador() == null || request.idItem() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Os IDs de jogador e item sao obrigatorios.");
        }
        Jogador jogador = jogadorDao.findById(request.idJogador());
        Item item = itemDao.findById(request.idItem());

        if (jogador == null || item == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do Jogador ou do Item invalido");
        }
        inventario.setJogador(jogador);
        inventario.setItem(item);

        Inventario inventario1 = inventarioDao.create(inventario);
        return ResponseEntity.status(HttpStatus.CREATED).body(InventarioResponse.fromEntity(inventario1));
    }

    @PostMapping("/update")
    @Operation(summary = "Atualizar inventario", description = "Atualiza uma inventario existente a partir do ID informado no corpo da requisicao.")
    public InventarioResponse atualizar(@RequestBody InventarioRequest request) {
        if (request.id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id obrigatorio para atualização");
        }
        if (request.idJogador() == null || request.idItem() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Os IDs de jogador e item sao obrigatorios.");
        }

        Inventario inventario = request.toEntity();
        Jogador jogador = jogadorDao.findById(request.idJogador());
        Item item = itemDao.findById(request.idItem());

        if (jogador == null || item == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do Jogador ou do Item invalido");
        }
        inventario.setJogador(jogador);
        inventario.setItem(item);

        return InventarioResponse.fromEntity(inventarioDao.update(inventario));
    }

    @PostMapping("/{id}/delete")
    @Operation(summary = "Remover inventario por ID", description = "Remove uma inventario existente pelo identificador.")
    public ResponseEntity<Void> removerPorId(@PathVariable long id) {
        if (!inventarioDao.deleteById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventario nao encontrado");
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/delete-all")
    @Operation(summary = "Remover todos os Inventarios", description = "Exclui todos os Inventarios cadastrados e retorna a quantidade removida.")
    public int removerTodos() {
        return 0;
    }
}
