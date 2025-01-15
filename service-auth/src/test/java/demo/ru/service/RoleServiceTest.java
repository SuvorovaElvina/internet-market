package demo.ru.service;

import demo.ru.dto.RoleDto;
import demo.ru.entity.Role;
import demo.ru.mapper.RoleMapper;
import demo.ru.repository.RoleRepository;
import demo.ru.throwable.NotFoundException;
import demo.ru.throwable.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RoleServiceTest {
    private final RoleRepository repository = mock(RoleRepository.class);
    private final RoleMapper mapper = mock(RoleMapper.class);

    private final RoleService service = new RoleService(repository, mapper);

    @Test
    void createNewRole() {
        RoleDto roleDto = RoleDto.builder().id(1).name("newRole").build();
        Role role = Role.builder().name("newRole").build();
        when(mapper.toRole(any())).thenReturn(role);
        when(mapper.toRoleDto(any())).thenReturn(roleDto);
        when(repository.findByName(anyString())).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(Role.builder().id(1).name("newRole").build());

        assertEquals(roleDto, service.createNewRole(roleDto), "Не создаётся новая роль");
    }

    @Test
    void createNewRoleFail() {
        RoleDto roleDto = RoleDto.builder().id(1).name("newRole").build();
        Role role = Role.builder().name("newRole").build();
        when(mapper.toRole(any())).thenReturn(role);
        when(repository.findByName(anyString())).thenReturn(Optional.of(role));

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            service.createNewRole(roleDto);
        });

        assertNotNull(thrown.getMessage(), "Роль создаётся при наличии этой же роли");
    }

    @Test
    void findById() {
        RoleDto roleDto = RoleDto.builder().id(1).name("test").build();
        when(mapper.toRoleDto(any())).thenReturn(roleDto);
        when(repository.findById(anyInt())).thenReturn(Optional.of(Role.builder().id(1).name("test").build()));

        assertEquals(roleDto, service.findById(1), "Не найдена роль по id");
    }

    @Test
    void notFindById() {
        when(mapper.toRoleDto(any())).thenReturn(RoleDto.builder().id(1).name("test").build());
        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        Throwable thrown = assertThrows(NotFoundException.class, () -> {
            service.findById(1);
        });

        assertNotNull(thrown.getMessage(), "Ошибку не выдаёт при отсутвии значения id");
    }

    @Test
    void getAllRole() {
        List<RoleDto> roles = List.of(RoleDto.builder().build(), RoleDto.builder().build());
        when(mapper.toRoleDto(any())).thenReturn(RoleDto.builder().id(1).name("test").build());
        when(repository.findAll()).thenReturn(List.of(Role.builder().build(), Role.builder().build()));

        assertEquals(roles.size(), service.getAllRole().size(), "Не совпадают размеры листов");
    }

    @Test
    void deleteRole() {
        service.deleteRole(1);
    }
}