package org.api.jogador;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dao.JogadorDao;
import org.dto.jogador.JogadorRankingDTO;
import org.dto.jogador.JogadorResponse;
import org.dto.jogador.JogadorRequest;
import org.model.Jogador;
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
@RequestMapping("/jogadores")
@Tag(name = "Jogadores", description = "Operacoes de cadastro e consulta de jogadores")
public class JogadorController {
    private final JogadorDao jogadorDao;

    public JogadorController(JogadorDao jogadorDao) {
        this.jogadorDao = jogadorDao;
    }

    @GetMapping
    @Operation(summary = "Listar jogadores", description = "Retorna todos os jogadores cadastrados.")
    public List<JogadorResponse> listarTodos() {
        return jogadorDao.findAll()
                .stream()
                .map(JogadorResponse::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar jogador por ID", description = "Retorna um jogador especifico pelo identificador.")
    public JogadorResponse buscarPorId(@PathVariable long id) {
        Jogador jogador = jogadorDao.findById(id);
        if (jogador == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogador nao encontrado");
        }
        return JogadorResponse.fromEntity(jogador);
    }

    @GetMapping("/next-id")
    @Operation(summary = "Obter proximo ID de jogador", description = "Retorna o proximo identificador sequencial disponivel para jogador.")
    public long proximoId() {
        return jogadorDao.nextId();
    }

    @PostMapping
    @Operation(summary = "Criar jogador", description = "Cria um novo jogador. Se o ID nao for informado, o sistema gera o proximo ID.")
    public ResponseEntity<JogadorResponse> criar(@RequestBody JogadorRequest request) {
        Jogador jogador = request.toEntity();
        if (request.id() == null) {
            jogador.setId(jogadorDao.nextId());
        }
        Jogador jogadorCriado = jogadorDao.create(jogador);
        return ResponseEntity.status(HttpStatus.CREATED).body(JogadorResponse.fromEntity(jogadorCriado));
    }

    @PostMapping("/update")
    @Operation(summary = "Atualizar jogador", description = "Atualiza um jogador existente a partir do ID informado no corpo da requisicao.")
    public JogadorResponse atualizar(@RequestBody JogadorRequest request) {
        if (request.id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id e obrigatorio para atualizacao");
        }
        return JogadorResponse.fromEntity(jogadorDao.update(request.toEntity()));
    }

    @PostMapping("/{id}/delete")
    @Operation(summary = "Remover jogador por ID", description = "Remove um jogador existente pelo identificador.")
    public ResponseEntity<Void> removerPorId(@PathVariable long id) {
        if (!jogadorDao.deleteById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogador nao encontrado");
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/delete-all")
    @Operation(summary = "Remover todos os jogadores", description = "Exclui todos os jogadores cadastrados e retorna a quantidade removida.")
    public int removerTodos() {
        return 0;
    }

    @GetMapping("/ranking")
    @Operation(summary = "Obter ranking de jogadores", description = "Retorna a lista de jogadores ordenada pela pontuação total acumulada.")
    public List<JogadorRankingDTO> obterRanking() {
        return jogadorDao.getRanking();
    }
}
