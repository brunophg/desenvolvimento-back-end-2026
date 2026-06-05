package org.api;

import org.api.item.ItemController;
import org.dao.ItemDao;
import org.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ItemControllerTest {

    private MockMvc mockMvc;
    private FakeItemDao itemDao;

    @BeforeEach
    void setUp() {
        itemDao = new FakeItemDao();
        mockMvc = MockMvcBuilders.standaloneSetup(new ItemController(itemDao)).build();
    }

    @Test
    void deveListarTodosOsItens() throws Exception {
        itemDao.itens = List.of(
                criarItem(1L, "Chuteira de Ouro", "Equipamento", 500.0),
                criarItem(2L, "Contrato Premium", "Consumivel", 150.0)
        );

        mockMvc.perform(get("/itens"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Chuteira de Ouro"))
                .andExpect(jsonPath("$[0].tipo").value("Equipamento"))
                .andExpect(jsonPath("$[0].valor").value(500.0))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].nome").value("Contrato Premium"))
                .andExpect(jsonPath("$[1].tipo").value("Consumivel"))
                .andExpect(jsonPath("$[1].valor").value(150.0));
    }

    @Test
    void deveBuscarItemPorId() throws Exception {
        itemDao.itemPorId = criarItem(10L, "Pacote de Estadio", "Cosmetico", 300.0);

        mockMvc.perform(get("/itens/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.nome").value("Pacote de Estadio"))
                .andExpect(jsonPath("$.valor").value(300.0));
    }

    @Test
    void deveRetornarNotFoundAoBuscarItemInexistente() throws Exception {
        mockMvc.perform(get("/itens/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarProximoId() throws Exception {
        itemDao.proximoId = 4001L;

        mockMvc.perform(get("/itens/next-id"))
                .andExpect(status().isOk())
                .andExpect(content().string("4001"));
    }

    @Test
    void deveCriarItemComIdInformado() throws Exception {
        itemDao.itemCriadoResposta = criarItem(20L, "Camisa Lendaria", "Cosmetico", 1000.0);

        mockMvc.perform(post("/itens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 20,
                                  "nome": "Camisa Lendaria",
                                  "tipo": "Cosmetico",
                                  "valor": 1000.0
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(20L))
                .andExpect(jsonPath("$.nome").value("Camisa Lendaria"));
    }

    @Test
    void deveCriarItemComProximoIdQuandoNaoInformado() throws Exception {
        itemDao.proximoId = 55L;
        itemDao.itemCriadoResposta = criarItem(55L, "Mochila Expansora", "Utilidade", 250.0);

        mockMvc.perform(post("/itens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Mochila Expansora",
                                  "tipo": "Utilidade",
                                  "valor": 250.0
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(55L))
                .andExpect(jsonPath("$.nome").value("Mochila Expansora"));

        org.junit.jupiter.api.Assertions.assertTrue(itemDao.nextIdChamado);
    }

    @Test
    void deveAtualizarItem() throws Exception {
        itemDao.itemAtualizadoResposta = criarItem(30L, "Bola de Ouro", "Equipamento", 800.0);

        mockMvc.perform(post("/itens/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 30,
                                  "nome": "Bola de Ouro",
                                  "tipo": "Equipamento",
                                  "valor": 800.0
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(30L))
                .andExpect(jsonPath("$.nome").value("Bola de Ouro"));
    }

    @Test
    void deveRetornarBadRequestAoAtualizarSemId() throws Exception {
        mockMvc.perform(post("/itens/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Bola de Ouro",
                                  "tipo": "Equipamento",
                                  "valor": 800.0
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRemoverItemPorId() throws Exception {
        itemDao.delecaoPorId = true;

        mockMvc.perform(post("/itens/40/delete"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarNotFoundAoRemoverItemInexistente() throws Exception {
        mockMvc.perform(post("/itens/41/delete"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRemoverTodosOsItens() throws Exception {
        mockMvc.perform(post("/itens/delete-all"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    private static Item criarItem(long id, String nome, String tipo, double valor) {
        Item item = new Item();
        item.setId(id);
        item.setNome(nome);
        item.setTipo(tipo);
        item.setValor(BigDecimal.valueOf(valor));
        return item;
    }

    private static final class FakeItemDao extends ItemDao {
        private List<Item> itens = new ArrayList<>();
        private Item itemPorId;
        private long proximoId;
        private boolean nextIdChamado;
        private Item itemCriadoResposta;
        private Item itemAtualizadoResposta;
        private boolean delecaoPorId;

        public FakeItemDao() {
        }

        @Override
        public List<Item> findAll() {
            return itens;
        }

        @Override
        public Item findById(long id) {
            return itemPorId;
        }

        @Override
        public long nextId() {
            nextIdChamado = true;
            return proximoId;
        }

        @Override
        public Item create(Item item) {
            return itemCriadoResposta != null ? itemCriadoResposta : item;
        }

        @Override
        public Item update(Item item) {
            return itemAtualizadoResposta != null ? itemAtualizadoResposta : item;
        }

        @Override
        public boolean deleteById(long id) {
            return delecaoPorId;
        }


    }
}