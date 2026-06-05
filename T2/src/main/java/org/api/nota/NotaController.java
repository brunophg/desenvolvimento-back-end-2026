package org.api.nota;

import org.dao.EmpresaDao;
import org.dao.NotaDao;
import org.dao.ParticipanteDao;
import org.model.Empresa;
import org.model.Nota;
import org.model.Participante;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/notas")
public class NotaController {

    private final NotaDao notaDao;
    private final EmpresaDao empresaDao;
    private final ParticipanteDao participanteDao;

    public NotaController(NotaDao notaDao, EmpresaDao empresaDao, ParticipanteDao participanteDao){
        this.notaDao = notaDao;
        this.empresaDao = empresaDao;
        this.participanteDao = participanteDao;
    }

    @GetMapping
    public List<NotaResponse> listarTodos() {
        return notaDao.findAll()
                .stream()
                .map(NotaResponse::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public NotaResponse buscarPorId(@PathVariable long id) {
        Nota nota = notaDao.findById(id);
        if (nota == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota não entcontrada");
        }
        return NotaResponse.fromEntity(nota);
    }

    @GetMapping("/next-id")
    public long proximoId(){
        return notaDao.nextId();
    }

    @PostMapping
    public ResponseEntity<NotaResponse> criar(@RequestBody NotaRequest request){
        Nota nota = request.toEntity();
        if (request.id() == null) {
            nota.setId(notaDao.nextId());
        }
        Empresa empresa = empresaDao.findById(request.idEmpresa());
        Participante participante = participanteDao.findById(request.idParticipante());

        if (empresa == null || participante == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID da Empresa ou do Participante inválido!");
        }
        nota.setEmpresa(empresa);
        nota.setParticipante(participante);

        Nota notaCriada = notaDao.create(nota);
        return ResponseEntity.status(HttpStatus.CREATED).body(NotaResponse.fromEntity(notaCriada));
    }

    @PostMapping("/update")
    public NotaResponse atualizar(@RequestBody NotaRequest request) {
        if (request.id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id obrigatorio para atualização");
        }
        Nota nota = request.toEntity();
        Empresa empresa = empresaDao.findById(request.idEmpresa());
        Participante participante = participanteDao.findById(request.idParticipante());

        if (empresa == null || participante == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID da Empresa ou do Participante inválido!");
        }
        nota.setEmpresa(empresa);
        nota.setParticipante(participante);

        return NotaResponse.fromEntity(notaDao.update(nota));
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> removerPorId(@PathVariable long id) {
        if (!notaDao.deleteById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota não encontrada");
        }
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/delete-all")
    public int removerTodos() {
        return notaDao.deleteAll();
    }


    }
