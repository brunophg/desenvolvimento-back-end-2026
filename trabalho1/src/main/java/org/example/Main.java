package org.example;

import org.config.JpaConnection;
import org.dao.*;
import org.model.Empresa;

public class Main {
    public static void main(String[] args) {

        // Inicializa DAOs usados na gravação das entidades.
        EmpresaDao empresaDao = new EmpresaDao();
        NotaDao notaDao = new NotaDao();
        ItemNotaDao itemNotaDao = new ItemNotaDao();
        ParticipanteDao participanteDao = new ParticipanteDao();
        ProdutoDao produtoDao = new ProdutoDao();

        try {
            empresaDao.deleteAll();
            notaDao.deleteAll();
            itemNotaDao.deleteAll();
            participanteDao.deleteAll();
            produtoDao.deleteAll();


            Empresa emp = new Empresa();
            emp.setId(empresaDao.nextId());
            emp.setRazaoSocial("Empresa de Teste Juiz de Fora");
            emp.setCnpj("00.000.000/0001-00");
            emp.setCodigo(101);
            empresaDao.create(emp);
            System.out.println("✅ Empresa criada com sucesso!");


        } catch (Exception e) {
            System.err.println("❌ Erro ao conectar ou salvar: " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            JpaConnection.close();
        }


    }
}