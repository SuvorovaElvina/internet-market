package demo.ru.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import demo.ru.dto.JwtRequest;
import demo.ru.dto.JwtResponse;
import demo.ru.dto.UserDto;
import demo.ru.service.AuthService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthorizationControllerTest {
    private final AuthService service = mock(AuthService.class);
    private final AuthorizationController controller = new AuthorizationController(service);
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createJwtToken() throws Exception {
        when(service.createJwtToken(any()))
                .thenReturn(new JwtResponse("token"));

        mvc.perform(post("/auth")
                        .content(mapper.registerModule(new JavaTimeModule())
                                .writeValueAsString(new JwtRequest("name", "Password")))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void createJwtTokenNotValidNameEmpty() throws Exception {
        when(service.createJwtToken(any()))
                .thenReturn(new JwtResponse("token"));

        mvc.perform(post("/auth")
                        .content(mapper.registerModule(new JavaTimeModule())
                                .writeValueAsString(new JwtRequest("", "Password")))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createJwtTokenNotValidNameNull() throws Exception {
        when(service.createJwtToken(any()))
                .thenReturn(new JwtResponse("token"));

        mvc.perform(post("/auth")
                        .content(mapper.registerModule(new JavaTimeModule())
                                .writeValueAsString(new JwtRequest(null, "Password")))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createJwtTokenNotValidNameBigLength() throws Exception {
        when(service.createJwtToken(any()))
                .thenReturn(new JwtResponse("token"));

        mvc.perform(post("/auth")
                        .content(mapper.registerModule(new JavaTimeModule())
                                .writeValueAsString(new JwtRequest("name is not valid and", "Password")))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createJwtTokenNotValidPasswordEmpty() throws Exception {
        when(service.createJwtToken(any()))
                .thenReturn(new JwtResponse("token"));

        mvc.perform(post("/auth")
                        .content(mapper.registerModule(new JavaTimeModule())
                                .writeValueAsString(new JwtRequest("name", "")))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createJwtTokenNotValidPasswordNull() throws Exception {
        when(service.createJwtToken(any()))
                .thenReturn(new JwtResponse("token"));

        mvc.perform(post("/auth")
                        .content(mapper.registerModule(new JavaTimeModule())
                                .writeValueAsString(new JwtRequest("name", null)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createJwtTokenNotValidPasswordBigLength() throws Exception {
        when(service.createJwtToken(any()))
                .thenReturn(new JwtResponse("token"));

        mvc.perform(post("/auth")
                        .content(mapper.registerModule(new JavaTimeModule())
                                .writeValueAsString(new JwtRequest("name", "Password is not valid")))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNewUser() throws Exception {
        UserDto userDto = UserDto.builder().name("name").password("Password").confirmPassword("Password").build();
        when(service.validAndCreateNewUser(any()))
                .thenReturn(userDto);

        mvc.perform(post("/register")
                        .content(mapper.registerModule(new JavaTimeModule())
                                .writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void createNewUserNotValidNameEmpty() throws Exception {
        UserDto userDto = UserDto.builder().name("").password("Password").confirmPassword("Password").build();
        when(service.validAndCreateNewUser(any()))
                .thenReturn(userDto);

        mvc.perform(post("/register")
                        .content(mapper.registerModule(new JavaTimeModule())
                                .writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNewUserNotValidNameNull() throws Exception {
        UserDto userDto = UserDto.builder().name(null).password("Password").confirmPassword("Password").build();
        when(service.validAndCreateNewUser(any()))
                .thenReturn(userDto);

        mvc.perform(post("/register")
                        .content(mapper.registerModule(new JavaTimeModule())
                                .writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNewUserNotValidNameBigLength() throws Exception {
        UserDto userDto = UserDto.builder().name("name is not valid and").password("Password").confirmPassword("Password").build();
        when(service.validAndCreateNewUser(any()))
                .thenReturn(userDto);

        mvc.perform(post("/register")
                        .content(mapper.registerModule(new JavaTimeModule())
                                .writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNewUserNotValidPasswordEmpty() throws Exception {
        UserDto userDto = UserDto.builder().name("name").password("").confirmPassword("Password").build();
        when(service.validAndCreateNewUser(any()))
                .thenReturn(userDto);

        mvc.perform(post("/register")
                        .content(mapper.registerModule(new JavaTimeModule())
                                .writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNewUserNotValidPasswordNull() throws Exception {
        UserDto userDto = UserDto.builder().name("name").password(null).confirmPassword("Password").build();
        when(service.validAndCreateNewUser(any()))
                .thenReturn(userDto);

        mvc.perform(post("/register")
                        .content(mapper.registerModule(new JavaTimeModule())
                                .writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNewUserNotValidPasswordBigLength() throws Exception {
        UserDto userDto = UserDto.builder().name("name").password("Password is not valid").confirmPassword("Password").build();
        when(service.validAndCreateNewUser(any()))
                .thenReturn(userDto);

        mvc.perform(post("/register")
                        .content(mapper.registerModule(new JavaTimeModule())
                                .writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNewUserNotValidConfirmPasswordNull() throws Exception {
        UserDto userDto = UserDto.builder().name("name").password("Password is not valid").confirmPassword(null).build();
        when(service.validAndCreateNewUser(any()))
                .thenReturn(userDto);

        mvc.perform(post("/register")
                        .content(mapper.registerModule(new JavaTimeModule())
                                .writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}