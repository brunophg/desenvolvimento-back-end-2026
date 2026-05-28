package org.api;

import org.api.jogo.JogoController;
import org.dao.JogoDao;
import org.model.Jogo;
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

class JogoControllerTest {

    private MockMvc mockMvc;
    private FakeJogoDao jogoDao;

    @BeforeEach
    void setUp() {
        jogoDao = new FakeJogoDao();
        mockMvc = MockMvcBuilders.standaloneSetup(new JogoController(jogoDao)).build();
    }

    @Test
    void deveListarTodosOsJogos() throws Exception {
        jogoDao.jogos = List.of(
                criarJogo(1L, "EA Sports FC 26", "Esportes"),
                criarJogo(2L, "Football Manager 2026", "Simulador")
        );

        mockMvc.perform(get("/jogos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("EA Sports FC 26"))
                .andExpect(jsonPath("$[0].genero").value("Esportes"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].nome").value("Football Manager 2026"))
                .andExpect(jsonPath("$[1].genero").value("Simulador"));
    }

    @Test
    void deveBuscarJogoPorId() throws Exception {
        jogoDao.jogoPorId = criarJogo(10L, "The Witcher 3", "RPG");

        mockMvc.perform(get("/jogos/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.nome").value("The Witcher 3"))
                .andExpect(jsonPath("$.genero").value("RPG"));
    }

    @Test
    void deveRetornarNotFoundAoBuscarJogoInexistente() throws Exception {
        mockMvc.perform(get("/jogos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarProximoId() throws Exception {
        jogoDao.proximoId = 4001L;

        mockMvc.perform(get("/jogos/next-id"))
                .andExpect(status().isOk())
                .andExpect(content().string("4001"));
    }

    @Test
    void deveCriarJogoComIdInformado() throws Exception {
        jogoDao.jogoCriadoResposta = criarJogo(20L, "Cyberpunk 2077", "RPG");

        mockMvc.perform(post("/jogos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 20,
                                  "nome": "Cyberpunk 2077",
                                  "genero": "RPG"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(20L))
                .andExpect(jsonPath("$.nome").value("Cyberpunk 2077"));
    }

    @Test
    void deveCriarJogoComProximoIdQuandoNaoInformado() throws Exception {
        jogoDao.proximoId = 55L;
        jogoDao.jogoCriadoResposta = criarJogo(55L, "Hollow Knight", "Metroidvania");

        mockMvc.perform(post("/jogos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Hollow Knight",
                                  "genero": "Metroidvania"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(55L))
                .andExpect(jsonPath("$.nome").value("Hollow Knight"));

        org.junit.jupiter.api.Assertions.assertTrue(jogoDao.nextIdChamado);
    }

    @Test
    void deveAtualizarJogo() throws Exception {
        jogoDao.jogoAtualizadoResposta = criarJogo(30L, "Hades", "Roguelike");

        mockMvc.perform(post("/jogos/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 30,
                                  "nome": "Hades",
                                  "genero": "Roguelike"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(30L))
                .andExpect(jsonPath("$.nome").value("Hades"));
    }

    @Test
    void deveRetornarBadRequestAoAtualizarSemId() throws Exception {
        mockMvc.perform(post("/jogos/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Hades",
                                  "genero": "Roguelike"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRemoverJogoPorId() throws Exception {
        jogoDao.delecaoPorId = true;

        mockMvc.perform(post("/jogos/40/delete"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarNotFoundAoRemoverJogoInexistente() throws Exception {
        mockMvc.perform(post("/jogos/41/delete"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRemoverTodosOsJogos() throws Exception {
        mockMvc.perform(post("/jogos/delete-all"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    private static Jogo criarJogo(long id, String nome, String genero) {
        Jogo jogo = new Jogo();
        jogo.setId(id);
        jogo.setNome(nome);
        jogo.setGenero(genero);
        return jogo;
    }

    private static final class FakeJogoDao extends JogoDao {
        private List<Jogo> jogos = new ArrayList<>();
        private Jogo jogoPorId;
        private long proximoId;
        private boolean nextIdChamado;
        private Jogo jogoCriadoResposta;
        private Jogo jogoAtualizadoResposta;
        private boolean delecaoPorId;

        public FakeJogoDao() {
        }

        @Override
        public List<Jogo> findAll() {
            return jogos;
        }

        @Override
        public Jogo findById(long id) {
            return jogoPorId;
        }

        @Override
        public long nextId() {
            nextIdChamado = true;
            return proximoId;
        }

        @Override
        public Jogo create(Jogo jogo) {
            return jogoCriadoResposta != null ? jogoCriadoResposta : jogo;
        }

        @Override
        public Jogo update(Jogo jogo) {
            return jogoAtualizadoResposta != null ? jogoAtualizadoResposta : jogo;
        }

        @Override
        public boolean deleteById(long id) {
            return delecaoPorId;
        }
    }
}