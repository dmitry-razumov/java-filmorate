package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Film {
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private long id;
    @NotBlank(message = "Имя фильма не должно быть пустым")
    private String name;
    @Size(max = MAX_DESCRIPTION_LENGTH, message = "Длина описания фильма не должна быть больше "
            + MAX_DESCRIPTION_LENGTH + " символов")
    private String description;
    @NotNull(message = "Дата релиза фильма не должна быть пустой")
    private LocalDate releaseDate;
    @Positive(message = "Длительность фильма должна быть больше 0")
    private long duration;
    private int rate = 0;
    private Set<Long> likes = new HashSet<>();
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();
    @NotNull
    private Mpa mpa;

    public int getLikesCount() {
        return likes.size();
    }
}
