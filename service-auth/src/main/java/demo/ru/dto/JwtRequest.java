package demo.ru.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtRequest {
    @NotNull
    @Size(min = 1, max = 20, message = "Name is not valid: min = 1, max = 20")
    String name;

    @NotNull
    @Size(min = 4, max = 20, message = "Password is not valid: min = 4, max = 20")
    String password;
}
