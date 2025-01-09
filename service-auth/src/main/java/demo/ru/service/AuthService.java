package demo.ru.service;

import demo.ru.dto.JwtRequest;
import demo.ru.dto.JwtResponse;
import demo.ru.dto.UserDto;
import demo.ru.throwable.AuthorizedException;
import demo.ru.throwable.ValidationException;
import demo.ru.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;


    public JwtResponse createJwtToken(JwtRequest request) {
        try {
            log.debug("Сервис авторизации: получен запрос на создание токена {}", request);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getName(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new AuthorizedException("Неправильный логин или пароль");
        }
        UserDetails userDetails = userService.loadUserByUsername(request.getName());
        String token = jwtTokenUtils.generateToken(userDetails);
        log.debug("Сервис авторизации: Создан токен {}", token);
        return new JwtResponse(token);
    }


    public UserDto validAndCreateNewUser(UserDto userDto) {
        log.debug("Сервис авторизации: получен запрос на регистрацию пользователя: {}", userDto);
        if (userDto.getPassword() != null) {
            if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
                throw new ValidationException("Пароли не совпадают");
            }

            if (userService.findByName(userDto.getName()) != null) {
                throw new ValidationException("Пользователь с этим именем уже существует");
            }
            log.debug("Сервис авторизации: пользователь прошёл валидацию");
            return userService.createNewUser(userDto);
        } else {
            throw new ValidationException("Пароль не указан");
        }
    }
}
