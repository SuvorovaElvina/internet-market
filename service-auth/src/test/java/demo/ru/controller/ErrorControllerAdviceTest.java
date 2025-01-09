package demo.ru.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import demo.ru.dto.JwtRequest;
import demo.ru.service.AuthService;
import demo.ru.throwable.AuthorizedException;
import demo.ru.throwable.NotFoundException;
import demo.ru.throwable.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ErrorControllerAdviceTest { //тестится не правильно
    private final AuthService service = mock(AuthService.class);
    private final AuthorizationController controller = new AuthorizationController(service);
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void handleNotFoundException() throws Exception {
        when(service.createJwtToken(any()))
                .thenThrow(new NotFoundException("Не найдено"));

        mvc.perform(post("/auth")
                        .content(mapper.registerModule(new JavaTimeModule())
                                .writeValueAsString(new JwtRequest("name", "Password")))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void handleValidationException() throws Exception {
        when(service.createJwtToken(any()))
                .thenThrow(new ValidationException("Валидация"));

        mvc.perform(post("/auth")
                        .content(mapper.registerModule(new JavaTimeModule())
                                .writeValueAsString(new JwtRequest("name", "Password")))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void handleAuthorizedException() throws Exception {
        when(service.createJwtToken(any()))
                .thenThrow(new AuthorizedException("Авторизация"));

        mvc.perform(post("/auth")
                        .content(mapper.registerModule(new JavaTimeModule())
                                .writeValueAsString(new JwtRequest("name", "Password")))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}