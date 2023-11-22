package ru.yandex.practicum.filmorate.model;

import lombok.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private long id;
    @NotEmpty(message = "email не должен быть пустым или null")
    @NotBlank(message = "email не должен быть пробелом или null")
    @Email(message = "недопустимый формат email")
    private String email;
    @NotBlank(message = "login не должен быть пробелом или null")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*]{1,}$", message = "недопустимые символы в login")
    private String login;
    private String name;
    @NotNull(message = "дата рождения не может быть пустой")
    @PastOrPresent(message = "дата рождения не может быть в будущем")
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();
}
