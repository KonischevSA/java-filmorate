package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserUpdateDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserModelsAndControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DirtiesContext
    void test_getWithEmptyUsersListShouldReturnEmptyBodyAndStatus200() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DirtiesContext
    void test_getWithNotEmptyUsersListShouldReturnUserAndStatus200() throws Exception {
        User user = new User();
        user.setEmail("aaa@aaa.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.now());

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(get("/users"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DirtiesContext
    void test_postWithValidUserShouldReturn200() throws Exception {
        User user = new User();
        user.setEmail("aaa@aaa.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.now());

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(200));
    }

    @Test
    @DirtiesContext
    void test_postWithNotValidEmailShouldReturn400() throws Exception {
        User user = new User();
        user.setEmail("aaa@aaaru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.now());

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(400));
    }

    @Test
    @DirtiesContext
    void test_postWithEmptyEmailShouldReturn400() throws Exception {
        User user = new User();
        user.setEmail(null);
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.now());

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(400));
    }

    @Test
    @DirtiesContext
    void test_postWithNotValidLoginShouldReturn400() throws Exception {
        User user = new User();
        user.setEmail("aaa@aaa.ru");
        user.setLogin("log in");
        user.setName("name");
        user.setBirthday(LocalDate.now());

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(400));
    }

    @Test
    @DirtiesContext
    void test_postWithEmptyLoginShouldReturn400() throws Exception {
        User user = new User();
        user.setEmail("aaa@aaa.ru");
        user.setLogin(null);
        user.setName("name");
        user.setBirthday(LocalDate.now());

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(400));
    }

    @Test
    @DirtiesContext
    void test_postWithBlankLoginShouldReturn400() throws Exception {
        User user = new User();
        user.setEmail("aaa@aaa.ru");
        user.setLogin("  ");
        user.setName("name");
        user.setBirthday(LocalDate.now());

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(400));
    }

    @Test
    @DirtiesContext
    void test_postWithEmptyNameShouldReturn200AndNameEqualsLogin() throws Exception {
        User user = new User();
        user.setEmail("aaa@aaa.ru");
        user.setLogin("login");
        user.setName(null);
        user.setBirthday(LocalDate.now());

        String json = objectMapper.writeValueAsString(user);

        String jsonResponse = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        User responceUser = objectMapper.readValue(jsonResponse, User.class);

        assertEquals(responceUser.getLogin(), responceUser.getName());
    }

    @Test
    @DirtiesContext
    void test_postWithEmptyBirthdayShouldReturn400() throws Exception {
        User user = new User();
        user.setEmail("aaa@aaa.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(null);

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(400));
    }

    @Test
    @DirtiesContext
    void test_postWithFutureBirthdayShouldReturn400() throws Exception {
        User user = new User();
        user.setEmail("aaa@aaa.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.now().plusDays(1));

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(400));
    }

    @Test
    @DirtiesContext
    void test_postWithDuplicateEmailShouldReturn400() throws Exception {
        User user1 = new User();
        user1.setEmail("aaa@aaa.ru");
        user1.setLogin("login");
        user1.setName("name");
        user1.setBirthday(LocalDate.now());

        User user2 = new User();
        user2.setEmail("aaa@aaa.ru");
        user2.setLogin("login2");
        user2.setName("name");
        user2.setBirthday(LocalDate.now());

        String json = objectMapper.writeValueAsString(user1);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        json = objectMapper.writeValueAsString(user2);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(400));
    }

    @Test
    @DirtiesContext
    void test_postWithDuplicateLoginShouldReturn400() throws Exception {
        User user1 = new User();
        user1.setEmail("aaa@bbb.ru");
        user1.setLogin("login");
        user1.setName("name");
        user1.setBirthday(LocalDate.now());

        User user2 = new User();
        user2.setEmail("aaa@aaa.ru");
        user2.setLogin("login");
        user2.setName("name");
        user2.setBirthday(LocalDate.now());

        String json = objectMapper.writeValueAsString(user1);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        json = objectMapper.writeValueAsString(user2);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(400));
    }

    @Test
    @DirtiesContext
    void test_putWithEmptyIdShouldReturn400() throws Exception {
        User user = new User();
        user.setEmail("aaa@aaa.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.now());

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        UserUpdateDto userDto = new UserUpdateDto();

        json = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(400));
    }

    @Test
    @DirtiesContext
    void test_putWithGoodIdShouldReturn200() throws Exception {
        User user = new User();
        user.setEmail("aaa@aaa.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.now());

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        UserUpdateDto userDto = new UserUpdateDto();
        userDto.setId(1L);

        json = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(200));
    }

    @Test
    @DirtiesContext
    void test_putUpdateEmailShouldReturnUpdatedUserAndStatus200() throws Exception {
        User user = new User();
        user.setEmail("aaa@aaa.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.now());

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        UserUpdateDto userDto = new UserUpdateDto();
        userDto.setId(1L);
        userDto.setEmail("bbb@bbb.ru");

        json = objectMapper.writeValueAsString(userDto);

        String jsonResponse = mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        User responceUser = objectMapper.readValue(jsonResponse, User.class);

        assertEquals(userDto.getEmail(), responceUser.getEmail());
    }

    @Test
    @DirtiesContext
    void test_putUpdateFreeLoginShouldReturnUpdatedUserAndStatus200() throws Exception {
        User user = new User();
        user.setEmail("aaa@aaa.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.now());

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        UserUpdateDto userDto = new UserUpdateDto();
        userDto.setId(1L);
        userDto.setLogin("new_login");

        json = objectMapper.writeValueAsString(userDto);

        String jsonResponse = mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        User responceUser = objectMapper.readValue(jsonResponse, User.class);

        assertEquals(userDto.getLogin(), responceUser.getLogin());
    }

    @Test
    @DirtiesContext
    void test_putUpdateDuplicateLoginShouldReturn400() throws Exception {
        User user1 = new User();
        user1.setEmail("aaa@aaa.ru");
        user1.setLogin("login");
        user1.setName("name");
        user1.setBirthday(LocalDate.now());

        User user2 = new User();
        user2.setEmail("bbb@aaa.ru");
        user2.setLogin("login2");
        user2.setName("name");
        user2.setBirthday(LocalDate.now());

        String json = objectMapper.writeValueAsString(user1);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        json = objectMapper.writeValueAsString(user2);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        UserUpdateDto userDto = new UserUpdateDto();
        userDto.setId(2L);
        userDto.setLogin("login");

        json = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(400))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
    }

    @Test
    @DirtiesContext
    void test_putUpdateNameShouldReturnUpdatedUserAndStatus200() throws Exception {
        User user = new User();
        user.setEmail("aaa@aaa.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.now());

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        UserUpdateDto userDto = new UserUpdateDto();
        userDto.setId(1L);
        userDto.setName("newName");

        json = objectMapper.writeValueAsString(userDto);

        String jsonResponse = mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        User responceUser = objectMapper.readValue(jsonResponse, User.class);

        assertEquals(userDto.getName(), responceUser.getName());
    }

    @Test
    @DirtiesContext
    void test_putUpdateBirthdayShouldReturnUpdatedUserAndStatus200() throws Exception {
        User user = new User();
        user.setEmail("aaa@aaa.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.now());

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        UserUpdateDto userDto = new UserUpdateDto();
        userDto.setId(1L);
        userDto.setBirthday(LocalDate.now().minusDays(1));

        json = objectMapper.writeValueAsString(userDto);

        String jsonResponse = mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        User responceUser = objectMapper.readValue(jsonResponse, User.class);

        assertEquals(userDto.getBirthday(), responceUser.getBirthday());
    }

    @Test
    @DirtiesContext
    void test_putWithMissingIdShouldReturn404() throws Exception {
        User user = new User();
        user.setEmail("aaa@aaa.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.now());

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        UserUpdateDto userDto = new UserUpdateDto();
        userDto.setId(2L);
        userDto.setBirthday(LocalDate.now().minusDays(1));

        json = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(404));
    }
}
