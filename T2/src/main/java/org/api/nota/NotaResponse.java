package org.api.nota;

import org.model.Nota;

import java.math.BigDecimal;
import java.util.Date;

public record NotaResponse(
        Long id,
        Date data,
        Integer numero,
        String razaoSocialEmpresa,
        String razaoSocialParticipante,
        BigDecimal valorTotal
)
{
    public static NotaResponse fromEntity(Nota nota){
        return new NotaResponse(
                nota.getId(),
                nota.getData(),
                nota.getNumero(),
                nota.getEmpresa() != null ? nota.getEmpresa().getRazaoSocial() : null,
                nota.getParticipante() != null ? nota.getParticipante().getRazaoSocial() : null,
                nota.getVrTotal()
                );
    }
}
