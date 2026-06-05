package org.api.participante;

import org.model.Participante;

public record ParticipanteRequest(
        Long id,
        Integer codigo,
        String razaoSocial,
        String cnpj
) {
    public Participante toEntity() {
        Participante participante = new Participante();
        if (id != null) {
            participante.setId(id);
        }
        participante.setCodigo(codigo);
        participante.setRazaoSocial(razaoSocial);
        participante.setCnpj(cnpj);
        return participante;
    }
}