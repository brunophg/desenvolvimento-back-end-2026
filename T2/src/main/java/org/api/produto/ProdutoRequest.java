package org.api.produto;

import org.model.Produto;

public record ProdutoRequest(
        Long id,
        String codigo,
        String descricao
) {
    public Produto toEntity() {
        Produto produto = new Produto();
        if (id != null) {
            produto.setId(id);
        }
        produto.setCodigo(codigo);
        produto.setDescricao(descricao);
        return produto;
    }
}
