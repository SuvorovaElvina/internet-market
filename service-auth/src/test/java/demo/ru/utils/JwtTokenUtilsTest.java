package demo.ru.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class JwtTokenUtilsTest {
    private static final JwtTokenUtils jwtToken = new JwtTokenUtils();
    private static final String secret = "1239q6ej2137djv23evqe123dqe25sfy5634jkibrtre";
    private static final Duration lifetime = Duration.parse("PT30S");

    @BeforeAll
    public static void setUp() {
        ReflectionTestUtils.setField(jwtToken, "secret", secret);
        ReflectionTestUtils.setField(jwtToken, "lifetime", lifetime);
    }

    @Test
    public void generateToken() {
        UserDetails userDetails = new User("Test", "test", List.of(new SimpleGrantedAuthority("USER")));
        String token = jwtToken.generateToken(userDetails);

        assertNotNull(token, "Не генерирует токен");
    }

    @Test
    void getUsername() {
        assertEquals("Test", jwtToken.getUsername(generateTokenByTest()), "Не получить имя из токена");
    }

    @Test
    void getRoles() {
        List<String> roles = List.of(new SimpleGrantedAuthority("ROLE_USER").getAuthority());
        assertEquals(roles, jwtToken.getRoles(generateTokenByTest()), "Не получить роли из токена");
    }

    private String generateTokenByTest() {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = List.of(new SimpleGrantedAuthority("ROLE_USER").getAuthority());
        claims.put("roles", roles);
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + lifetime.toMillis());
        return Jwts.builder().claims().add(claims)
                .subject("Test")
                .issuedAt(issuedDate)
                .expiration(expiredDate)
                .and().signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)))
                .compact();
    }
}