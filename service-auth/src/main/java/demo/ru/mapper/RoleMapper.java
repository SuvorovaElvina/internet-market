package demo.ru.mapper;

import demo.ru.dto.RoleDto;
import demo.ru.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    public RoleDto toRoleDto(Role role) {
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }

    public Role toRole(RoleDto roleDto) {
        return Role.builder()
                .name(roleDto.getName())
                .build();
    }
}
