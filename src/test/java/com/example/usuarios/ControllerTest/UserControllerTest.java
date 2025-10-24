package com.example.usuarios.ControllerTest;

import com.example.usuarios.model.User;
import com.example.usuarios.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

public class UserControllerTest {

@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Simula o repositório sem acessar banco real
    @MockBean
    private UserRepository userRepository;

    @Test
    void deveListarUsuarios() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("Thiago");
        user.setEmail("thiago@email.com");
        user.setAge(25);

        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Thiago"))
                .andExpect(jsonPath("$[0].email").value("thiago@email.com"));
    }

    @Test
    void deveRetornarUsuarioPorId() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("Maria");
        user.setEmail("maria@email.com");
        user.setAge(30);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Maria"))
                .andExpect(jsonPath("$.email").value("maria@email.com"));
    }

    @Test
    void deveCriarUsuario() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("João");
        user.setEmail("joao@email.com");
        user.setAge(20);

        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("João"))
                .andExpect(jsonPath("$.email").value("joao@email.com"));
    }

    @Test
    void deveAtualizarUsuarioExistente() throws Exception {
        User existente = new User();
        existente.setId(1L);
        existente.setName("Ana");
        existente.setEmail("ana@email.com");
        existente.setAge(22);

        User atualizado = new User();
        atualizado.setName("Ana Silva");
        atualizado.setEmail("ana.silva@email.com");
        atualizado.setAge(23);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(userRepository.save(any(User.class))).thenReturn(atualizado);

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ana Silva"))
                .andExpect(jsonPath("$.email").value("ana.silva@email.com"));
    }

    @Test
    void deveExcluirUsuarioExistente() throws Exception {
        when(userRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarNotFoundAoExcluirUsuarioInexistente() throws Exception {
        when(userRepository.existsById(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/users/99"))
                .andExpect(status().isNotFound());
    }

}
