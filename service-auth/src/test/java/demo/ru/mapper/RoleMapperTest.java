package demo.ru.mapper;

import demo.ru.dto.RoleDto;
import demo.ru.entity.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleMapperTest {
    private final RoleMapper mapper = new RoleMapper();

    @Test
    void toRoleDto() {
        Role role = Role.builder().id(1).name("test").build();
        RoleDto roleDto = RoleDto.builder().id(1).name("test").build();

        RoleDto mapRole = mapper.toRoleDto(role);

        assertEquals(roleDto, mapRole, "Не правильная работа RoleMapper в методе toRoleDto");
    }

    @Test
    void toRole() {
        Role role = Role.builder().name("test").build();
        RoleDto roleDto = RoleDto.builder().name("test").build();

        Role mapRole = mapper.toRole(roleDto);

        assertEquals(role, mapRole, "Не правильная работа RoleMapper в методе toRole");
    }
}