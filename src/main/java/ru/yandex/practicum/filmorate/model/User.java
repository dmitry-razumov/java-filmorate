package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
