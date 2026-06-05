package org.api.itemNota;

import org.dao.ItemNotaDao;
import org.dao.NotaDao;
import org.model.ItemNota;
import org.model.Nota;
import org.model.Produto;

import java.math.BigDecimal;

public record ItemNotaRequest(
        Long id,
        BigDecimal vrUnitario,
        BigDecimal quantidade,
        Long idNota,
        Long idProduto
) {
    public ItemNota toEntity() {
        ItemNota item = new ItemNota();
        if (id != null) {
            item.setId(id);
        }
        item.setVrUnitario(vrUnitario);
        item.setQuantidade(quantidade);
        if (idNota != null) {
            Nota nota = new Nota();
            nota.setId(idNota);
            new NotaDao().findById(idNota);
            item.setNota(nota);
        }
        if (idProduto != null) {
            Produto prod = new Produto();
            prod.setId(idProduto);
            item.setProduto(prod);
        }
        return item;
    }
}
