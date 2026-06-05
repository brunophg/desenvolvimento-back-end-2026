package org.api.participante;

import org.dao.ParticipanteDao;
import org.model.Participante;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/participantes")
public class ParticipanteController {

    private final ParticipanteDao participanteDao;

    public ParticipanteController(ParticipanteDao participanteDao) {
        this.participanteDao = participanteDao;
    }

    @GetMapping
    public List<ParticipanteResponse> listarTodos() {
        return participanteDao.findAll()
                .stream()
                .map(ParticipanteResponse::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public ParticipanteResponse buscarPorId(@PathVariable long id) {
        Participante participante = participanteDao.findById(id);
        if (participante == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Participante não encontrado");
        }
        return ParticipanteResponse.fromEntity(participante);
    }

    @GetMapping("/next-id")
    public Long buscarProximoId() {
        return participanteDao.nextId();
    }

    @PostMapping
    public ResponseEntity<ParticipanteResponse> criar(@RequestBody ParticipanteRequest request) {
        Participante participante = request.toEntity();

        if (request.id() == null) {
            participante.setId(participanteDao.nextId());
        }

        Participante participanteCriado = participanteDao.create(participante);
        return ResponseEntity.status(HttpStatus.CREATED).body(ParticipanteResponse.fromEntity(participanteCriado));
    }

    @PostMapping("/update")
    public ParticipanteResponse atualizar(@RequestBody ParticipanteRequest request) {
        if (request.id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id obrigatório para atualização");
        }
        Participante participante = request.toEntity();
        return ParticipanteResponse.fromEntity(participanteDao.update(participante));
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> removerPorId(@PathVariable long id) {
        if (!participanteDao.deleteById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Participante não encontrado");
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/delete-all")
    public int removerTodos() {
        return participanteDao.deleteAll();
    }
}