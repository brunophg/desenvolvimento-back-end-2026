package org.api.nota;

import org.model.Empresa;
import org.model.Nota;
import org.model.Participante;

import java.util.Date;

public record NotaRequest(
        Long id,
        Date data,
        Integer numero,
        Long idEmpresa,
        Long idParticipante
) {
    public Nota toEntity() {
        Nota nota = new Nota();
        if (id != null){
            nota.setId(id);
        }
        nota.setData(data);
        nota.setNumero(numero);
        if (this.idEmpresa != null) {
            Empresa e = new Empresa();
            e.setId(this.idEmpresa);
            nota.setEmpresa(e);
        }

        if (this.idParticipante != null) {
            Participante p = new Participante();
            p.setId(this.idParticipante);
            nota.setParticipante(p);
        }
    return nota;
    }
}
