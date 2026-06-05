package org.api.produto;

import org.model.Produto;

public record ProdutoResponse(
        Long id,
        String codigo,
        String descricao
) {
    public static ProdutoResponse fromEntity(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getCodigo(),
                produto.getDescricao()
        );
    }
}