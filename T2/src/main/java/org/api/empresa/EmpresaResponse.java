package org.api.empresa;

import org.model.Empresa;
import org.springframework.http.ResponseEntity;

public record EmpresaResponse(
        Long id,
        Integer codigo,
        String razaoSocial,
        String endereco,
        String cnpj
) {
    public static EmpresaResponse fromEntity(Empresa empresa){
        return new EmpresaResponse(
                empresa.getId(),
                empresa.getCodigo(),
                empresa.getRazaoSocial(),
                empresa.getEndereco(),
                empresa.getCnpj()
        );
    }
}
