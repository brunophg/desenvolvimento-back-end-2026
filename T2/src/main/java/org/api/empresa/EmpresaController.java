package org.api.empresa;


import org.api.nota.NotaResponse;
import org.dao.EmpresaDao;
import org.model.Empresa;
import org.model.Nota;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    private final EmpresaDao empresaDao;

    public EmpresaController(EmpresaDao empresaDao){
        this.empresaDao = empresaDao;
    }

    @GetMapping
    public List<EmpresaResponse> listarTodos() {
        return empresaDao.findAll()
                .stream()
                .map(EmpresaResponse::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public EmpresaResponse buscarPorId(@PathVariable long id) {
        Empresa empresa = empresaDao.findById(id);
        if (empresa == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não entcontrada");
        }
        return EmpresaResponse.fromEntity(empresa);
    }

    @GetMapping("/next-id")
    public long proximoId(){
        return empresaDao.nextId();
    }

    @PostMapping
    public ResponseEntity<EmpresaResponse> criar(@RequestBody EmpresaRequest request) {
        Empresa empresa = request.toEntity();
        if (request.id() == null) {
            empresa.setId(empresaDao.nextId());
        }

        Empresa empresaCriada = empresaDao.create(empresa);
        return ResponseEntity.status(HttpStatus.CREATED).body(EmpresaResponse.fromEntity(empresaCriada));
    }

    @PostMapping("/update")
    public EmpresaResponse atualizar(@RequestBody EmpresaRequest request) {
        if (request.id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id obrigatorio para atualização");
        }
        Empresa empresa = request.toEntity();
        return EmpresaResponse.fromEntity(empresaDao.update(request.toEntity()));
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> removerPorId(@PathVariable long id){
        if (!empresaDao.deleteById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada");
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/delete-all")
    public int removerTodos() {
        return empresaDao.deleteAll();
    }

}
