package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    private final User user1 = new User(1L, "Евгений", "eugene@mail.ru");
    private final User user2 = new User(2L, "Петр", "petr@mail.ru");
    private final User user3 = new User(3L, "Ольга", "olga@mail.ru");

    @Autowired
    ObjectMapper mapper;
    @MockBean
    UserService userService;
    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("Получить список пользователей")
    void getUsers() throws Exception {
        List<User> users = List.of(user1, user2, user3);
        when(userService.getAllUsers())
                .thenReturn(users);

        List<UserDto> userDtoList = UserMapper.getUserDtoList(users);
        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(userDtoList)));
    }

    @Test
    @DisplayName("Получить пользователя по id")
    void getUserById() throws Exception {
        when(userService.getUser(anyLong()))
                .thenReturn(user1);

        mvc.perform(get("/users/{userId}", user1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user1.getName())))
                .andExpect(jsonPath("$.email", is(user1.getEmail())));
    }

    @Test
    @DisplayName("Добавить пользователя")
    void addUser() throws Exception {
        when(userService.create(any()))
                .thenReturn(user1);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(UserMapper.toUserDto(user1)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user1.getName())))
                .andExpect(jsonPath("$.email", is(user1.getEmail())));
    }

    @Test
    @DisplayName("Обновить информацию о пользователе")
    void updateUser() throws Exception {
        when(userService.edit(any(), any()))
                .thenReturn(user1);

        mvc.perform(patch("/users/{userId}", user1.getId())
                        .content(mapper.writeValueAsString(UserMapper.toUserDto(user1)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user1.getName())))
                .andExpect(jsonPath("$.email", is(user1.getEmail())));
    }

    @Test
    @DisplayName("Удалить пользователя")
    void deleteUser() throws Exception {
        mvc.perform(delete("/users/{userId}", user1.getId()))
                .andExpect(status().isOk());
    }
}
