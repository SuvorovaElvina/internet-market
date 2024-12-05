package demo.ru.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;

    @NotNull
    @Size(min = 1, max = 200, message = "Name is not valid: min = 1, max = 200")
    String name;

    @NotNull
    @Size(min = 4, max = 20, message = "Password is not valid: min = 4, max = 20")
    String password;

    @NotNull
    String confirmPassword;
}
