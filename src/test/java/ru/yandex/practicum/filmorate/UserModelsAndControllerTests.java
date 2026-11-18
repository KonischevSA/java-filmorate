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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    void test_getUserByIdShouldReturn200() throws Exception {
        User user = new User();
        user.setEmail("aaa@aaa.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.now());

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("aaa@aaa.ru"))
                .andExpect(jsonPath("$.login").value("login"))
                .andExpect(jsonPath("$.name").value("name"));
    }

    @Test
    @DirtiesContext
    void test_getUserWithMissingIdShouldReturn404() throws Exception {
        User user = new User();
        user.setEmail("aaa@aaa.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.now());

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(get("/users/2"))
                .andExpect(status().is(404));
    }

    @Test
    @DirtiesContext
    void test_getUserFriendsShouldReturn200() throws Exception {
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

        mockMvc.perform(put("/users/1/friends/2"));

        mockMvc.perform(get("/users/2/friends"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].friends.length()").value(1))
                .andExpect(jsonPath("$[0].friends[0]").value(2));
    }

    @Test
    @DirtiesContext
    void test_getUserFriendsWithMissingUserIdShouldReturn404() throws Exception {
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

        mockMvc.perform(put("/users/1/friends/2"));

        mockMvc.perform(get("/users/3/friends"))
                .andExpect(status().is(404));
    }

    @Test
    @DirtiesContext
    void test_getUserCommonFriendsShouldReturn200() throws Exception {
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

        User user3 = new User();
        user3.setEmail("ccc@aaa.ru");
        user3.setLogin("login3");
        user3.setName("name");
        user3.setBirthday(LocalDate.now());

        String json = objectMapper.writeValueAsString(user1);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        json = objectMapper.writeValueAsString(user2);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        json = objectMapper.writeValueAsString(user3);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(put("/users/1/friends/2"));
        mockMvc.perform(put("/users/1/friends/3"));

        mockMvc.perform(get("/users/2/friends/common/3"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DirtiesContext
    void test_getUserCommonFriendsWithMissingUserIdShouldReturn404() throws Exception {
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

        User user3 = new User();
        user3.setEmail("ccc@aaa.ru");
        user3.setLogin("login3");
        user3.setName("name");
        user3.setBirthday(LocalDate.now());

        String json = objectMapper.writeValueAsString(user1);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        json = objectMapper.writeValueAsString(user2);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        json = objectMapper.writeValueAsString(user3);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(put("/users/1/friends/2"));
        mockMvc.perform(put("/users/1/friends/3"));

        mockMvc.perform(get("/users/5/friends/common/3"))
                .andExpect(status().is(404));
    }

    @Test
    @DirtiesContext
    void test_getUserCommonFriendsWithMissingOtherUserIdShouldReturn404() throws Exception {
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

        User user3 = new User();
        user3.setEmail("ccc@aaa.ru");
        user3.setLogin("login3");
        user3.setName("name");
        user3.setBirthday(LocalDate.now());

        String json = objectMapper.writeValueAsString(user1);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        json = objectMapper.writeValueAsString(user2);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        json = objectMapper.writeValueAsString(user3);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(put("/users/1/friends/2"));
        mockMvc.perform(put("/users/1/friends/3"));

        mockMvc.perform(get("/users/2/friends/common/5"))
                .andExpect(status().is(404));
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
    void test_postWithDuplicateEmailShouldReturn409() throws Exception {
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
                .andExpect(status().is(409));
    }

    @Test
    @DirtiesContext
    void test_postWithDuplicateLoginShouldReturn409() throws Exception {
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
                .andExpect(status().is(409));
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
    void test_putUpdateDuplicateLoginShouldReturn409() throws Exception {
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
                .andExpect(status().is(409))
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

    @Test
    @DirtiesContext
    void test_putFriendShouldReturn200() throws Exception {
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

        mockMvc.perform(put("/users/1/friends/2"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.friends.length()").value(1))
                .andExpect(jsonPath("$.friends[0]").value(2));
    }

    @Test
    @DirtiesContext
    void test_putFriendWithMissingUserIdShouldReturn404() throws Exception {
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

        mockMvc.perform(put("/users/3/friends/2"))
                .andExpect(status().is(404));
    }

    @Test
    @DirtiesContext
    void test_putFriendWithMissingFriendIdShouldReturn404() throws Exception {
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

        mockMvc.perform(put("/users/1/friends/3"))
                .andExpect(status().is(404));
    }

    @Test
    @DirtiesContext
    void test_putFriendWithEqualsUserIdAndFriendIdShouldReturn409() throws Exception {
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

        mockMvc.perform(put("/users/1/friends/1"))
                .andExpect(status().is(409));
    }

    @Test
    @DirtiesContext
    void test_deleteFriendShouldReturn200() throws Exception {
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

        mockMvc.perform(put("/users/1/friends/2"));

        mockMvc.perform(delete("/users/1/friends/2"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.friends.length()").value(0));
    }

    @Test
    @DirtiesContext
    void test_deleteFriendWithMissingUserIdShouldReturn404() throws Exception {
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

        mockMvc.perform(put("/users/1/friends/2"));

        mockMvc.perform(delete("/users/3/friends/2"))
                .andExpect(status().is(404));
    }

    @Test
    @DirtiesContext
    void test_deleteFriendWithMissingFriendIdShouldReturn404() throws Exception {
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

        mockMvc.perform(put("/users/1/friends/2"));

        mockMvc.perform(delete("/users/1/friends/3"))
                .andExpect(status().is(404));
    }

    @Test
    @DirtiesContext
    void test_deleteFriendWithEqualsUserIdAndFriendIdShouldReturn409() throws Exception {
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

        mockMvc.perform(put("/users/1/friends/2"));

        mockMvc.perform(delete("/users/1/friends/1"))
                .andExpect(status().is(409));
    }

    @Test
    @DirtiesContext
    void test_deleteUserShouldReturn204() throws Exception {
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

        mockMvc.perform(put("/users/1/friends/2"));

        mockMvc.perform(get("/users/2"))
                .andExpect(jsonPath("$.friends.length()").value(1));

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().is(204));

        mockMvc.perform(get("/users/2"))
                .andExpect(jsonPath("$.friends.length()").value(0));
    }

    @Test
    @DirtiesContext
    void test_deleteUserWithMissingShouldReturn404() throws Exception {
        User user1 = new User();
        user1.setEmail("aaa@aaa.ru");
        user1.setLogin("login");
        user1.setName("name");
        user1.setBirthday(LocalDate.now());

        String json = objectMapper.writeValueAsString(user1);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(delete("/users/2"))
                .andExpect(status().is(404));
    }
}
