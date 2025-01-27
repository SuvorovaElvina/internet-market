package demo.ru.controller;

import demo.ru.dto.JwtRequest;
import demo.ru.dto.JwtResponse;
import demo.ru.dto.UserDto;
import demo.ru.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthorizationController {
    private final AuthService authService;

    @PostMapping("/auth")
    @ResponseStatus(HttpStatus.CREATED)
    public JwtResponse createJwtToken(@Valid @RequestBody JwtRequest request) {
        log.debug("Контроллер: Запрос на создание токена: {}", request);
        return authService.createJwtToken(request);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createNewUser(@Valid @RequestBody UserDto userDto) {
        log.debug("Контроллер: Регистрация нового пользователя: {}", userDto);
        return authService.validAndCreateNewUser(userDto);
    }
}
