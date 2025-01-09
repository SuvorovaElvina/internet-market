package demo.ru.controller;

import demo.ru.dto.RoleDto;
import demo.ru.service.RoleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public RoleDto createNewRole(@Valid @RequestBody RoleDto roleDto) {
        log.debug("Контроллер: Запрос на создание новой роли: {}", roleDto);
        return roleService.createNewRole(roleDto);
    }

    @GetMapping("/get")
    public List<RoleDto> getAllRole() {
        log.debug("Контроллер: Запрос на получение списка всех ролей");
        return roleService.getAllRole();
    }

    @GetMapping("/get/{id}")
    public RoleDto getRoleById(@PathVariable @PositiveOrZero Integer id) {
        log.debug("Контроллер: Запрос на получение роли с id = {}", id);
        return roleService.findById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteRoleById(@PathVariable @PositiveOrZero Integer id) {
        log.debug("Контроллер: Запрос на удаление роли с id = {}", id);
        roleService.deleteRole(id);
    }
}
