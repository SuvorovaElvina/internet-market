package demo.ru.mapper;

import demo.ru.dto.UserDto;
import demo.ru.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
    private final UserMapper mapper = new UserMapper();
    private final PasswordEncoder password = new BCryptPasswordEncoder();

    @Test
    void toUser() {
        User user = User.builder().name("dto").password(password.encode("test")).build();
        UserDto userDto = UserDto.builder().name("dto").password("test").build();

        User mapUser = mapper.toUser(userDto);

        assertEquals(user.getPassword().length(), mapUser.getPassword().length(), "Не правильная работа UserMapper в методе toUser");
        assertEquals(user.getName(), mapUser.getName(), "Не правильная работа UserMapper в методе toUser");
    }

    @Test
    void toUserDto() {
        User user = User.builder().id(1L).name("dto").password("test").build();
        UserDto userDto = UserDto.builder().id(1L).name("dto").password("test").build();

        UserDto mapUser = mapper.toUserDto(user);

        assertEquals(userDto, mapUser, "Не правильная работа UserMapper в методе toUserDto");
    }
}