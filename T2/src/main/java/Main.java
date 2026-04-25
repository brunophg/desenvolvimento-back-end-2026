import org.config.JpaConnection;
import org.dao.*;
import org.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        EmpresaDao empresaDao = new EmpresaDao();
        NotaDao notaDao = new NotaDao();
        ItemNotaDao itemNotaDao = new ItemNotaDao();
        ParticipanteDao participanteDao = new ParticipanteDao();
        ProdutoDao produtoDao = new ProdutoDao();


        try {
            System.out.println("Limpando banco de dados para o teste...");
            itemNotaDao.deleteAll();
            notaDao.deleteAll();
            produtoDao.deleteAll();
            participanteDao.deleteAll();
            empresaDao.deleteAll();

            Empresa emp = new Empresa();
            emp.setId(empresaDao.nextId());
            emp.setRazaoSocial("Empresa de Teste Juiz de Fora");
            emp.setCnpj("00.000.000/0001-00");
            emp.setCodigo(101);
            empresaDao.create(emp);
            System.out.println("Empresa criada com sucesso!");

            Participante part = new Participante();
            part.setId(participanteDao.nextId());
            part.setRazaoSocial("Joao Silva LTDA");
            part.setCnpj("11.111.111/0001-11");
            part.setCodigo(202);
            participanteDao.create(part);
            System.out.println("Participante criado!");

            Produto prod = new Produto();
            prod.setId(produtoDao.nextId());
            prod.setCodigo("505");
            prod.setDescricao("Notebook Gamer");
            produtoDao.create(prod);
            System.out.println("Produto criado!");

            Nota nota = new Nota();
            nota.setId(notaDao.nextId());
            nota.setNumero(1001);
            nota.setData(new Date());
            nota.setEmpresa(emp);
            nota.setParticipante(part);
            notaDao.create(nota);
            System.out.println("Nota Fiscal nº 1001 criada!");

            ItemNota item = new ItemNota();
            item.setId(itemNotaDao.nextId());
            item.setNota(nota);
            item.setProduto(prod);
            item.setQuantidade(new BigDecimal("2.00"));
            item.setVrUnitario(new BigDecimal("2000.00"));
            itemNotaDao.create(item);
            System.out.println("✅ Item adicionado à nota (2x Notebook)!");

            if (nota.getItensNota() == null) {
                nota.setItensNota(new ArrayList<>());
            }
            nota.getItensNota().add(item);

            System.out.println("\n========================================");
            System.out.println("Finalizado");
            System.out.println("VALOR TOTAL DA NOTA: R$ " + nota.getVrTotal());

        } catch (Exception e) {
            System.err.println("Erro durante a execução: " + e.getMessage());
        } finally {
            JpaConnection.close();
        }
    }
}