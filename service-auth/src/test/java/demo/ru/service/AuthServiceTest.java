package demo.ru.service;

import demo.ru.dto.JwtRequest;
import demo.ru.dto.JwtResponse;
import demo.ru.dto.UserDto;
import demo.ru.entity.User;
import demo.ru.throwable.AuthorizedException;
import demo.ru.throwable.ValidationException;
import demo.ru.utils.JwtTokenUtils;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceTest {
    private final UserService userService = mock(UserService.class);
    private final JwtTokenUtils jwtTokenUtils = mock(JwtTokenUtils.class);
    private final AuthenticationManager manager = mock(AuthenticationManager.class);

    private final AuthService service = new AuthService(userService, jwtTokenUtils, manager);

    @Test
    void createJwtToken() {
        JwtResponse response = new JwtResponse("test");
        when(manager.authenticate(any())).thenReturn(authenticationEmptyForTest());
        when(userService.loadUserByUsername(anyString())).thenReturn(new org.springframework.security.core.userdetails.User(
                "name", "password", List.of(new SimpleGrantedAuthority("ROLE_USER"))
        ));
        when(jwtTokenUtils.generateToken(any())).thenReturn("test");

        assertEquals(response, service.createJwtToken(new JwtRequest("name", "password")),
                "Не создаёт токен при правильных данных");
    }

    @Test
    void createJwtTokenWithAuthenticationException() {
        when(manager.authenticate(any())).thenThrow(new BadCredentialsException(""));
        Throwable thrown = assertThrows(AuthorizedException.class, () -> {
            service.createJwtToken(new JwtRequest("name", "password"));
        });

        assertNotNull(thrown.getMessage(), "Не выкидывает ошибку в случае отсутвия повторного пароля");
    }

    @Test
    void validAndCreateNewUser() {
        UserDto userDto = UserDto.builder().password("123").confirmPassword("123").build();
        when(userService.findByName(anyString())).thenReturn(null);
        when(userService.createNewUser(any())).thenReturn(userDto);

        assertEquals(userDto, service.validAndCreateNewUser(userDto), "Ошибочная валидация при правильных данных");
    }

    @Test
    void validAndCreateNewUserWithoutConfirmPassword() {
        UserDto userDto = UserDto.builder().password("123").build();

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            service.validAndCreateNewUser(userDto);
        });

        assertNotNull(thrown.getMessage(), "Не выкидывает ошибку в случае отсутвия повторного пароля");
    }

    @Test
    void validAndCreateNewUserWithFailConfirmPassword() {
        UserDto userDto = UserDto.builder().password("123").confirmPassword("111").build();

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            service.validAndCreateNewUser(userDto);
        });

        assertNotNull(thrown.getMessage(), "Не выкидывает ошибку в случае ошибочного повторного пароля");
    }

    @Test
    void validAndCreateNewUserWithoutPassword() {
        UserDto userDto = UserDto.builder().confirmPassword("123").build();

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            service.validAndCreateNewUser(userDto);
        });

        assertNotNull(thrown.getMessage(), "Не выкидывает ошибку в случае отсутвия основного пароля");
    }

    @Test
    void validAndCreateNewUserWhenUserExists() {
        UserDto userDto = UserDto.builder().password("123").confirmPassword("123").name("test").build();
        when(userService.findByName(anyString())).thenReturn(User.builder().name("test").build());
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            service.validAndCreateNewUser(userDto);
        });

        assertNotNull(thrown.getMessage(), "Не выкидывает ошибку в случае уже существующего пользователя");
    }

    private Authentication authenticationEmptyForTest() {
        return new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return null;
            }
        };
    }
}