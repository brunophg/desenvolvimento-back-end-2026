package org.api;

import org.api.inventario.InventarioController;
import org.dao.InventarioDao;
import org.model.Inventario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InventarioControllerTest {

    private MockMvc mockMvc;
    private FakeInventarioDao inventarioDao;

    @BeforeEach
    void setUp() {
        inventarioDao = new FakeInventarioDao();

        // Fake rápido para o Jogador
        org.dao.JogadorDao jogadorDaoFake = new org.dao.JogadorDao() {
            @Override
            public org.model.Jogador findById(long id) {
                return new org.model.Jogador();
            }
        };

        // Fake rápido para o Item
        org.dao.ItemDao itemDaoFake = new org.dao.ItemDao() {
            @Override
            public org.model.Item findById(long id) {
                return new org.model.Item();
            }
        };

        mockMvc = MockMvcBuilders.standaloneSetup(
                new InventarioController(inventarioDao, jogadorDaoFake, itemDaoFake)
        ).build();
    }

    @Test
    void deveListarTodosOsInventarios() throws Exception {
        inventarioDao.inventarios = List.of(
                criarInventario(1L, 2),
                criarInventario(2L, 5)
        );

        mockMvc.perform(get("/inventarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].quantidade").value(2))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].quantidade").value(5));
    }

    @Test
    void deveBuscarInventarioPorId() throws Exception {
        inventarioDao.inventarioPorId = criarInventario(10L, 1);

        mockMvc.perform(get("/inventarios/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.quantidade").value(1));
    }

    @Test
    void deveRetornarNotFoundAoBuscarInventarioInexistente() throws Exception {
        mockMvc.perform(get("/inventarios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarProximoId() throws Exception {
        inventarioDao.proximoId = 4001L;

        mockMvc.perform(get("/inventarios/next-id"))
                .andExpect(status().isOk())
                .andExpect(content().string("4001"));
    }

    @Test
    void deveCriarInventarioComIdInformado() throws Exception {
        inventarioDao.inventarioCriadoResposta = criarInventario(20L, 10);

        mockMvc.perform(post("/inventarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 20,
                                  "quantidade": 10,
                                  "idJogador": 4001,
                                  "idItem": 4001
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(20L))
                .andExpect(jsonPath("$.quantidade").value(10));
    }

    @Test
    void deveCriarInventarioComProximoIdQuandoNaoInformado() throws Exception {
        inventarioDao.proximoId = 55L;
        inventarioDao.inventarioCriadoResposta = criarInventario(55L, 3);

        mockMvc.perform(post("/inventarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "quantidade": 3,
                                  "idJogador": 4002,
                                  "idItem": 4002
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(55L))
                .andExpect(jsonPath("$.quantidade").value(3));

        org.junit.jupiter.api.Assertions.assertTrue(inventarioDao.nextIdChamado);
    }

    @Test
    void deveAtualizarInventario() throws Exception {
        inventarioDao.inventarioAtualizadoResposta = criarInventario(30L, 7);

        mockMvc.perform(post("/inventarios/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 30,
                                  "quantidade": 7,
                                  "idJogador": 4003,
                                  "idItem": 4003
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(30L))
                .andExpect(jsonPath("$.quantidade").value(7));
    }

    @Test
    void deveRetornarBadRequestAoAtualizarSemId() throws Exception {
        mockMvc.perform(post("/inventarios/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "quantidade": 7,
                                  "idJogador": 4003,
                                  "idItem": 4003
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRemoverInventarioPorId() throws Exception {
        inventarioDao.delecaoPorId = true;

        mockMvc.perform(post("/inventarios/40/delete"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarNotFoundAoRemoverInventarioInexistente() throws Exception {
        mockMvc.perform(post("/inventarios/41/delete"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRemoverTodosOsInventarios() throws Exception {
        mockMvc.perform(post("/inventarios/delete-all"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    private static Inventario criarInventario(long id, int quantidade) {
        Inventario inventario = new Inventario();
        inventario.setId(id);
        inventario.setQuantidade(quantidade);
        return inventario;
    }

    private static final class FakeInventarioDao extends InventarioDao {
        private List<Inventario> inventarios = new ArrayList<>();
        private Inventario inventarioPorId;
        private long proximoId;
        private boolean nextIdChamado;
        private Inventario inventarioCriadoResposta;
        private Inventario inventarioAtualizadoResposta;
        private boolean delecaoPorId;

        public FakeInventarioDao() {
        }

        @Override
        public List<Inventario> findAll() {
            return inventarios;
        }

        @Override
        public Inventario findById(long id) {
            return inventarioPorId;
        }

        @Override
        public long nextId() {
            nextIdChamado = true;
            return proximoId;
        }

        @Override
        public Inventario create(Inventario inventario) {
            return inventarioCriadoResposta != null ? inventarioCriadoResposta : inventario;
        }

        @Override
        public Inventario update(Inventario inventario) {
            return inventarioAtualizadoResposta != null ? inventarioAtualizadoResposta : inventario;
        }

        @Override
        public boolean deleteById(long id) {
            return delecaoPorId;
        }
    }
}