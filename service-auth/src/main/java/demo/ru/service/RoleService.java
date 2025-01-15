package demo.ru.service;

import demo.ru.dto.RoleDto;
import demo.ru.entity.Role;
import demo.ru.mapper.RoleMapper;
import demo.ru.repository.RoleRepository;
import demo.ru.throwable.NotFoundException;
import demo.ru.throwable.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository repository;
    private final RoleMapper mapper;

    public RoleDto createNewRole(RoleDto roleDto) {
        log.debug("Сервис ролей: Получен запрос на создание: {}", roleDto);
        Role role = mapper.toRole(roleDto);
        if (repository.findByName(role.getName()).isPresent()) {
            throw new ValidationException("Такая роль уже существует");
        }
        return mapper.toRoleDto(repository.save(role));
    }

    public RoleDto findById(Integer id) {
        log.debug("Сервис ролей: Получен запрос на получение по id {}", id);
        return mapper.toRoleDto(repository.findById(id).orElseThrow(() -> new NotFoundException("Роль не найдена")));
    }

    public List<RoleDto> getAllRole() {
        log.debug("Сервис ролей: Получен запрос на получение всех ролей");
        return repository.findAll().stream().map(mapper::toRoleDto).toList();
    }

    public void deleteRole(Integer id) {
        log.debug("Сервис ролей: Получен запрос на удаление роли с id {}", id);
        repository.deleteById(id);
    }

}
