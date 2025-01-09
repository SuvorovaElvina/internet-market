package demo.ru.service;

import demo.ru.dto.UserDto;
import demo.ru.entity.Role;
import demo.ru.entity.User;
import demo.ru.mapper.UserMapper;
import demo.ru.repository.RoleRepository;
import demo.ru.repository.UserRepository;
import demo.ru.throwable.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper mapper;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByName(username);

        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(),
                mapRoleToAuthorities(user.getRoles()));
    }

    public UserDto createNewUser(UserDto userDto) {
        log.debug("Сервис пользователей: Получени запрос на создание нового пользователя {}", userDto);
        User user = mapper.toUser(userDto);
        user.setRoles(List.of(roleRepository.findByName("ROLE_USER").orElseThrow(() ->
                new NotFoundException("Невозможно зарегистрироваться. Возможно роль была удалена."))));
        return mapper.toUserDto(userRepository.save(user));
    }


    public User findByName(String name) {
        log.debug("Сервис пользователей: Получени запрос на получения пользователя по имени {}", name);
        if (userRepository.findByName(name).isPresent()) {
            return userRepository.findByName(name).get();
        }
        return null;
    }

    private List<GrantedAuthority> mapRoleToAuthorities(List<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
