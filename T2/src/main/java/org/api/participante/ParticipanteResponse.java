package org.api.participante;

import org.model.Participante;

public record ParticipanteResponse(
        Long id,
        Integer codigo,
        String razaoSocial,
        String cnpj
) {
    public static ParticipanteResponse fromEntity(Participante participante) {
        return new ParticipanteResponse(
                participante.getId(),
                participante.getCodigo(),
                participante.getRazaoSocial(),
                participante.getCnpj()
        );
    }
}