package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @NotNull
    private LocalDate releaseDate;
    @Positive
    private long duration;
    Set<Long> likes = new HashSet<>();

    public int getNumberOfLikes() {
        return likes.size();
    }
}
