package org.api;

import org.api.jogador.JogadorController;
import org.dao.JogadorDao;
import org.model.Jogador;
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

class JogadorControllerTest {
    private MockMvc mockMvc;
    private FakeJogadorDao jogadorDao;

    @BeforeEach
    void setUp() {
        jogadorDao = new FakeJogadorDao();
        mockMvc = MockMvcBuilders.standaloneSetup(new JogadorController(jogadorDao)).build();
    }

    @Test
    void deveListarTodosOsJogadores() throws Exception {
        jogadorDao.jogadores = List.of(criarJogador(1L, "Bruno"), criarJogador(2L, "Kevin"));

        mockMvc.perform(get("/jogadores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Bruno"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].nome").value("Kevin"));
    }

    @Test
    void deveBuscarJogadorPorId() throws Exception {
        jogadorDao.jogadorPorId = criarJogador(10L, "Pedro");

        mockMvc.perform(get("/jogadores/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.nome").value("Pedro"))
                .andExpect(jsonPath("$.nickname").value("nickname10"));
    }

    @Test
    void deveRetornarNotFoundAoBuscarJogadorInexistente() throws Exception {
        mockMvc.perform(get("/jogadores/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarProximoId() throws Exception {
        jogadorDao.proximoId = 4001L;

        mockMvc.perform(get("/jogadores/next-id"))
                .andExpect(status().isOk())
                .andExpect(content().string("4001"));
    }

    @Test
    void deveCriarJogadorComIdInformado() throws Exception {
        jogadorDao.jogadorCriadoResposta = criarJogador(20L, "Daniel");

        mockMvc.perform(post("/jogadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 20,
                                  "nome": "Daniel",
                                  "nickname": "dani20",
                                  "email": "daniel@email.com"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(20L))
                .andExpect(jsonPath("$.nome").value("Daniel"));
    }

    @Test
    void deveCriarJogadorComProximoIdQuandoNaoInformado() throws Exception {
        jogadorDao.proximoId = 55L;
        jogadorDao.jogadorCriadoResposta = criarJogador(55L, "Elaine");

        mockMvc.perform(post("/jogadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Elaine",
                                  "nickname": "elaine55",
                                  "email": "elaine@email.com"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(55L))
                .andExpect(jsonPath("$.nome").value("Elaine"));

        org.junit.jupiter.api.Assertions.assertTrue(jogadorDao.nextIdChamado);
    }

    @Test
    void deveAtualizarJogador() throws Exception {
        jogadorDao.jogadorAtualizadoResposta = criarJogador(30L, "Fabio");

        mockMvc.perform(post("/jogadores/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 30,
                                  "nome": "Fabio",
                                  "nickname": "fabio30",
                                  "email": "fabio@email.com"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(30L))
                .andExpect(jsonPath("$.nome").value("Fabio"));
    }

    @Test
    void deveRetornarBadRequestAoAtualizarSemId() throws Exception {
        mockMvc.perform(post("/jogadores/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Fabio",
                                  "nickname": "fabio30",
                                  "email": "fabio@email.com"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRemoverJogadorPorId() throws Exception {
        jogadorDao.delecaoPorId = true;

        mockMvc.perform(post("/jogadores/40/delete"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarNotFoundAoRemoverJogadorInexistente() throws Exception {
        mockMvc.perform(post("/jogadores/41/delete"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRemoverTodosOsJogadores() throws Exception {
        jogadorDao.quantidadeRemovida = 3;

        mockMvc.perform(post("/jogadores/delete-all"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    private static Jogador criarJogador(long id, String nome) {
        Jogador jogador = new Jogador();
        jogador.setId(id);
        jogador.setNome(nome);
        jogador.setNickname("nickname" + id);
        jogador.setEmail("email" + id + "@teste.com");
        return jogador;
    }

    private static final class FakeJogadorDao extends JogadorDao {
        private List<Jogador> jogadores = new ArrayList<>();
        private Jogador jogadorPorId;
        private long proximoId;
        private boolean nextIdChamado;
        private Jogador jogadorCriadoResposta;
        private Jogador jogadorAtualizadoResposta;
        private boolean delecaoPorId;
        private int quantidadeRemovida;

        @Override
        public List<Jogador> findAll() {
            return jogadores;
        }

        @Override
        public Jogador findById(long id) {
            return jogadorPorId;
        }

        @Override
        public long nextId() {
            nextIdChamado = true;
            return proximoId;
        }

        @Override
        public Jogador create(Jogador jogador) {
            return jogadorCriadoResposta != null ? jogadorCriadoResposta : jogador;
        }

        @Override
        public Jogador update(Jogador jogador) {
            return jogadorAtualizadoResposta != null ? jogadorAtualizadoResposta : jogador;
        }

        @Override
        public boolean deleteById(long id) {
            return delecaoPorId;
        }

    }
}