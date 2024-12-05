package demo.ru.controller;

import demo.ru.dto.RoleDto;
import demo.ru.service.RoleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

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
    public RoleDto getRole(@RequestParam Integer id) {
        log.debug("Контроллер: Запрос на получение роли с id = {}", id);
        return roleService.findById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteRole(@RequestParam Integer id) {
        log.debug("Контроллер: Запрос на удаление роли с id = {}", id);
        roleService.deleteRole(id);
    }
}
