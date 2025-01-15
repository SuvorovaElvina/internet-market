package demo.ru.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import demo.ru.dto.RoleDto;
import demo.ru.service.RoleService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RoleControllerTest {
    private final RoleService service = mock(RoleService.class);
    private final RoleController controller = new RoleController(service);
    private final ObjectMapper mapper = new ObjectMapper();
    private final Duration lifetime = Duration.parse("PT30S");
    private MockMvc mvc;
    private RoleDto roleDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();

        roleDto = RoleDto.builder().id(1).name("Test").build();
    }

    @Test
    void createNewRole() throws Exception {
        when(service.createNewRole(any()))
                .thenReturn(roleDto);

        mvc.perform(post("/roles/create")
                        .header("token", generateTokenByTest())
                        .content(mapper.registerModule(new JavaTimeModule())
                                .writeValueAsString(roleDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void getAllRole() throws Exception {
        when(service.getAllRole())
                .thenReturn(List.of(roleDto));

        mvc.perform(get("/roles/get")
                        .header("token", generateTokenByTest())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getRoleById() throws Exception {
        when(service.findById(1))
                .thenReturn(roleDto);

        mvc.perform(get("/roles/get/1")
                        .header("token", generateTokenByTest())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getRoleByIdIsZero() throws Exception {
        when(service.findById(0))
                .thenReturn(roleDto);

        mvc.perform(get("/roles/get/0")
                        .header("token", generateTokenByTest())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getRoleByIdIsNegative() throws Exception {
        try {
        when(service.findById(1))
                .thenReturn(roleDto);

        mvc.perform(get("/roles/get/-1")
                        .header("token", generateTokenByTest())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        } catch (ServletException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    void deleteRoleById() throws Exception {
        mvc.perform(delete("/roles/delete/1")
                        .header("token", generateTokenByTest())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteRoleByIdIsZero() throws Exception {
        mvc.perform(delete("/roles/delete/0")
                        .header("token", generateTokenByTest())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteRoleByIdNegative() throws Exception {
        try {
        mvc.perform(delete("/roles/delete/1")
                        .header("token", generateTokenByTest())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        } catch (ServletException e) {
            assertNotNull(e.getMessage());
        }
    }

    private String generateTokenByTest() {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = List.of(new SimpleGrantedAuthority("ROLE_ADMIN").getAuthority());
        claims.put("roles", roles);
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + lifetime.toMillis());
        String secret = "1239q6ej2137djv23evqe123dqe25sfy5634jkibrtre";
        return Jwts.builder().claims().add(claims)
                .subject("Test")
                .issuedAt(issuedDate)
                .expiration(expiredDate)
                .and().signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)))
                .compact();
    }
}