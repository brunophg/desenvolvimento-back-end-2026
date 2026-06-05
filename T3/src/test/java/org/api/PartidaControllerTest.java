package org.api;

import org.api.partida.PartidaController;
import org.dao.PartidaDao;
import org.model.Partida;
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

class PartidaControllerTest {

    private MockMvc mockMvc;
    private FakePartidaDao partidaDao;

    @BeforeEach
    void setUp() {
        partidaDao = new FakePartidaDao();

        // Fake rápido para o Jogador
        org.dao.JogadorDao jogadorDaoFake = new org.dao.JogadorDao() {
            @Override
            public org.model.Jogador findById(long id) {
                return new org.model.Jogador();
            }
        };

        // Fake rápido para o Jogo
        org.dao.JogoDao jogoDaoFake = new org.dao.JogoDao() {
            @Override
            public org.model.Jogo findById(long id) {
                return new org.model.Jogo();
            }
        };

        mockMvc = MockMvcBuilders.standaloneSetup(
                new PartidaController(partidaDao, jogadorDaoFake, jogoDaoFake)
        ).build();
    }

    @Test
    void deveListarTodasAsPartidas() throws Exception {
        partidaDao.partidas = List.of(
                criarPartida(1L, 1500),
                criarPartida(2L, 1200)
        );

        mockMvc.perform(get("/partidas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].pontuacao").value(1500))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].pontuacao").value(1200));
    }

    @Test
    void deveBuscarPartidaPorId() throws Exception {
        partidaDao.partidaPorId = criarPartida(10L, 1800);

        mockMvc.perform(get("/partidas/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.pontuacao").value(1800));
    }

    @Test
    void deveRetornarNotFoundAoBuscarPartidaInexistente() throws Exception {
        mockMvc.perform(get("/partidas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarProximoId() throws Exception {
        partidaDao.proximoId = 4001L;

        mockMvc.perform(get("/partidas/next-id"))
                .andExpect(status().isOk())
                .andExpect(content().string("4001"));
    }

    @Test
    void deveCriarPartidaComIdInformado() throws Exception {
        partidaDao.partidaCriadaResposta = criarPartida(20L, 2000);

        mockMvc.perform(post("/partidas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 20,
                                  "data": "2026-05-20T10:00:00",
                                  "pontuacao": 2000,
                                  "idJogador": 4001,
                                  "idJogo": 4001
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(20L))
                .andExpect(jsonPath("$.pontuacao").value(2000));
    }

    @Test
    void deveCriarPartidaComProximoIdQuandoNaoInformado() throws Exception {
        partidaDao.proximoId = 55L;
        partidaDao.partidaCriadaResposta = criarPartida(55L, 3000);

        mockMvc.perform(post("/partidas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "data": "2026-05-21T14:00:00",
                                  "pontuacao": 3000,
                                  "idJogador": 4002,
                                  "idJogo": 4002
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(55L))
                .andExpect(jsonPath("$.pontuacao").value(3000));

        org.junit.jupiter.api.Assertions.assertTrue(partidaDao.nextIdChamado);
    }

    @Test
    void deveAtualizarPartida() throws Exception {
        partidaDao.partidaAtualizadaResposta = criarPartida(30L, 4000);

        mockMvc.perform(post("/partidas/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 30,
                                  "data": "2026-05-22T16:15:00",
                                  "pontuacao": 4000,
                                  "idJogador": 4001,
                                  "idJogo": 4002
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(30L))
                .andExpect(jsonPath("$.pontuacao").value(4000));
    }

    @Test
    void deveRetornarBadRequestAoAtualizarSemId() throws Exception {
        mockMvc.perform(post("/partidas/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "data": "2026-05-22T16:15:00",
                                  "pontuacao": 4000,
                                  "idJogador": 4001,
                                  "idJogo": 4002
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRemoverPartidaPorId() throws Exception {
        partidaDao.delecaoPorId = true;

        mockMvc.perform(post("/partidas/40/delete"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarNotFoundAoRemoverPartidaInexistente() throws Exception {
        mockMvc.perform(post("/partidas/41/delete"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRemoverTodasAsPartidas() throws Exception {
        mockMvc.perform(post("/partidas/delete-all"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    private static Partida criarPartida(long id, int pontuacao) {
        Partida partida = new Partida();
        partida.setId(id);
        partida.setPontuacao(pontuacao);
        return partida;
    }

    private static final class FakePartidaDao extends PartidaDao {
        private List<Partida> partidas = new ArrayList<>();
        private Partida partidaPorId;
        private long proximoId;
        private boolean nextIdChamado;
        private Partida partidaCriadaResposta;
        private Partida partidaAtualizadaResposta;
        private boolean delecaoPorId;

        public FakePartidaDao() {
        }

        @Override
        public List<Partida> findAll() {
            return partidas;
        }

        @Override
        public Partida findById(long id) {
            return partidaPorId;
        }

        @Override
        public long nextId() {
            nextIdChamado = true;
            return proximoId;
        }

        @Override
        public Partida create(Partida partida) {
            return partidaCriadaResposta != null ? partidaCriadaResposta : partida;
        }

        @Override
        public Partida update(Partida partida) {
            return partidaAtualizadaResposta != null ? partidaAtualizadaResposta : partida;
        }

        @Override
        public boolean deleteById(long id) {
            return delecaoPorId;
        }

    }
}