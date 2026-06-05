package org.api.empresa;

import org.model.Empresa;

public record EmpresaRequest(
        Long id,
        Integer codigo,
        String razaoSocial,
        String endereco,
        String cnpj
) {
    public Empresa toEntity() {
        Empresa emp = new Empresa();
        if (id != null){
            emp.setId(id);
        }
        emp.setCodigo(codigo);
        emp.setRazaoSocial(razaoSocial);
        emp.setEndereco(endereco);
        emp.setCnpj(cnpj);

        return emp;
    }
}
