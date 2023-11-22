package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.time.LocalDate;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private UserController userController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldBeSuccessValidationsForValidUserData() throws Exception {
        User user = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1946,8,20))
                .build();
        String validUser = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(validUser))
                .andDo(h -> assertEquals(200, h.getResponse().getStatus()));

        mockMvc.perform(get("/users")
                .contentType("application/json"))
                .andDo(h -> assertEquals(200, h.getResponse().getStatus()));

        mockMvc.perform(get("/users/1")
                .contentType("application/json"))
                .andDo(h -> assertEquals(200, h.getResponse().getStatus()));

        mockMvc.perform(get("/users/2")
                .contentType("application/json"))
                .andDo(h -> assertEquals(404, h.getResponse().getStatus()));

        mockMvc.perform(delete("/users/1")
                .contentType("application/json"))
                .andDo(h -> assertEquals(200, h.getResponse().getStatus()));

        assertEquals(Collections.EMPTY_LIST, userController.getAll());
    }

    @Test
    void shouldBe400ResponseForInvalidUserData() throws Exception {
        String invalidUser = objectMapper.writeValueAsString(User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail.ru") // неверный формат
                .birthday(LocalDate.of(1980,8,20))
                .build());
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(invalidUser))
                .andDo(h -> assertEquals(400, h.getResponse().getStatus()));
    }

    @Test
    void shouldBe500ResponseForEmptyBodyRequest() throws Exception {
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(""))
                .andDo(h -> assertEquals(500, h.getResponse().getStatus()));
    }

    @Test
    void shouldBe404ResponseForWrongURI() throws Exception {
        mockMvc.perform(get("/user")
                .contentType("application/json"))
                .andDo(h -> assertEquals(404, h.getResponse().getStatus()));
    }
}
