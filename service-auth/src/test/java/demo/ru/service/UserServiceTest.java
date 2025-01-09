package demo.ru.service;

import demo.ru.dto.UserDto;
import demo.ru.entity.Role;
import demo.ru.entity.User;
import demo.ru.mapper.UserMapper;
import demo.ru.repository.RoleRepository;
import demo.ru.repository.UserRepository;
import demo.ru.throwable.NotFoundException;
import demo.ru.throwable.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final RoleRepository roleRepository = mock(RoleRepository.class);
    private final UserMapper mapper = mock(UserMapper.class);

    private final UserService service = new UserService(userRepository, roleRepository, mapper);

    @Test
    void loadUserByUsername() {
        User user = User.builder().name("test").password("TEST")
                .roles(List.of(Role.builder().name("ROLE_USER").build())).build();
        when(userRepository.findByName(anyString())).thenReturn(Optional.of(user));
        UserDetails userDetails = new org.springframework.security.core.userdetails.User("test", "TEST",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        assertEquals(userDetails, service.loadUserByUsername("test"), "Не совпадают UserDetails и User из базы данных");
    }

    @Test
    void createNewUser() {
        UserDto userDto = UserDto.builder().name("test").password("Test").build();
        User user = User.builder().name("test").password("Test").build();
        when(mapper.toUser(any())).thenReturn(user);
        when(mapper.toUserDto(any())).thenReturn(userDto);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(Role.builder().name("newRole").build()));
        when(userRepository.save(any())).thenReturn(user);

        assertEquals(userDto, service.createNewUser(userDto), "Не создаётся новая роль");
    }

    @Test
    void createNewUserFail() {
        UserDto userDto = UserDto.builder().name("test").password("Test").build();
        User user = User.builder().name("test").password("Test").build();
        when(mapper.toUser(any())).thenReturn(user);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

        Throwable thrown = assertThrows(NotFoundException.class, () -> {
            service.createNewUser(userDto);
        });

        assertNotNull(thrown.getMessage(), "Создаётся пользователь с несуществующей ролью");
    }

    @Test
    void findByName() {
        User user = User.builder().id(1L).name("test").roles(List.of(Role.builder().name("USER").build())).build();
        when(userRepository.findByName(anyString())).thenReturn(Optional.of(user));

        assertEquals(user, service.findByName("Test"), "Не найден пользователь, когда обязан быть");
    }

    @Test
    void findByNameEmpty() {
        when(userRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertNull(service.findByName("test"), "Не возвращает null при отсутвии пользователя");
    }
}