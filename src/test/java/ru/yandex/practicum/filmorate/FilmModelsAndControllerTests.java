package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmUpdateDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmModelsAndControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DirtiesContext
    void test_getWithEmptyFilmsListShouldReturnEmptyBodyAndStatus200() throws Exception {
        mockMvc.perform(get("/films"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DirtiesContext
    void test_getWithNotEmptyFilmsListShouldReturnFilmAndStatus200() throws Exception {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.now().minusYears(1));
        film.setDuration(100);

        String json = objectMapper.writeValueAsString(film);

        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(get("/films"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DirtiesContext
    void test_postWithValidFilmShouldReturn200() throws Exception {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.now().minusYears(1));
        film.setDuration(100);

        String json = objectMapper.writeValueAsString(film);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(200));
    }

    @Test
    @DirtiesContext
    void test_postWithNotValidNameShouldReturn400() throws Exception {
        Film film = new Film();
        film.setName(" ");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.now().minusYears(1));
        film.setDuration(100);

        String json = objectMapper.writeValueAsString(film);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(400));
    }

    @Test
    @DirtiesContext
    void test_postWithEmptyNameShouldReturn400() throws Exception {
        Film film = new Film();
        film.setName(null);
        film.setDescription("description");
        film.setReleaseDate(LocalDate.now().minusYears(1));
        film.setDuration(100);

        String json = objectMapper.writeValueAsString(film);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(400));
    }

    @Test
    @DirtiesContext
    void test_postWithNotValidDescriptionShouldReturn400() throws Exception {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description".repeat(20));
        film.setReleaseDate(LocalDate.now().minusYears(1));
        film.setDuration(100);

        String json = objectMapper.writeValueAsString(film);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(400));
    }

    @Test
    @DirtiesContext
    void test_postWithNotValidReleaseDateShouldReturn400() throws Exception {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(1000, Month.DECEMBER, 1));
        film.setDuration(100);

        String json = objectMapper.writeValueAsString(film);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(400));
    }

    @Test
    @DirtiesContext
    void test_postWithNotValidDurationShouldReturn400() throws Exception {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.now().minusYears(1));
        film.setDuration(-100);

        String json = objectMapper.writeValueAsString(film);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(400));
    }

    @Test
    @DirtiesContext
    void test_postWithEmptyDurationShouldReturn400() throws Exception {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.now().minusYears(1));
        film.setDuration(null);

        String json = objectMapper.writeValueAsString(film);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(400));
    }

    @Test
    @DirtiesContext
    void test_postDuplicateFilmShouldReturn400() throws Exception {
        Film film1 = new Film();
        film1.setName("name");
        film1.setDescription("description");
        film1.setReleaseDate(LocalDate.now().minusYears(1));
        film1.setDuration(100);

        Film film2 = new Film();
        film2.setName("name");
        film2.setDescription("description");
        film2.setReleaseDate(LocalDate.now().minusYears(1));
        film2.setDuration(100);

        String json = objectMapper.writeValueAsString(film1);

        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        json = objectMapper.writeValueAsString(film2);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(400));
    }

    @Test
    @DirtiesContext
    void test_putWithEmptyIdShouldReturn400() throws Exception {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.now().minusYears(1));
        film.setDuration(100);

        String json = objectMapper.writeValueAsString(film);

        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        FilmUpdateDto filmDto = new FilmUpdateDto();

        json = objectMapper.writeValueAsString(filmDto);

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(400));
    }

    @Test
    @DirtiesContext
    void test_putWithGoodIdShouldReturn200() throws Exception {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.now().minusYears(1));
        film.setDuration(100);

        String json = objectMapper.writeValueAsString(film);

        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        FilmUpdateDto filmDto = new FilmUpdateDto();
        filmDto.setId(1L);

        json = objectMapper.writeValueAsString(filmDto);

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(200));
    }

    @Test
    @DirtiesContext
    void test_putUpdateNameShouldReturnUpdatedFilmAndStatus200() throws Exception {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.now().minusYears(1));
        film.setDuration(100);

        String json = objectMapper.writeValueAsString(film);

        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        FilmUpdateDto filmDto = new FilmUpdateDto();
        filmDto.setId(1L);
        filmDto.setName("new_name");

        json = objectMapper.writeValueAsString(filmDto);

        String jsonResponse = mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Film responceFilm = objectMapper.readValue(jsonResponse, Film.class);

        assertEquals(filmDto.getName(), responceFilm.getName());
    }

    @Test
    @DirtiesContext
    void test_putUpdateDescriptionShouldReturnUpdatedFilmAndStatus200() throws Exception {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.now().minusYears(1));
        film.setDuration(100);

        String json = objectMapper.writeValueAsString(film);

        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        FilmUpdateDto filmDto = new FilmUpdateDto();
        filmDto.setId(1L);
        filmDto.setDescription("new_description");

        json = objectMapper.writeValueAsString(filmDto);

        String jsonResponse = mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Film responceFilm = objectMapper.readValue(jsonResponse, Film.class);

        assertEquals(filmDto.getDescription(), responceFilm.getDescription());
    }

    @Test
    @DirtiesContext
    void test_putUpdateReleaseDateShouldReturnUpdatedFilmAndStatus200() throws Exception {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.now().minusYears(1));
        film.setDuration(100);

        String json = objectMapper.writeValueAsString(film);

        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        FilmUpdateDto filmDto = new FilmUpdateDto();
        filmDto.setId(1L);
        filmDto.setReleaseDate(LocalDate.now().minusYears(2));

        json = objectMapper.writeValueAsString(filmDto);

        String jsonResponse = mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Film responceFilm = objectMapper.readValue(jsonResponse, Film.class);

        assertEquals(filmDto.getReleaseDate(), responceFilm.getReleaseDate());
    }

    @Test
    @DirtiesContext
    void test_putUpdateDurationShouldReturnUpdatedFilmAndStatus200() throws Exception {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.now().minusYears(1));
        film.setDuration(100);

        String json = objectMapper.writeValueAsString(film);

        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        FilmUpdateDto filmDto = new FilmUpdateDto();
        filmDto.setId(1L);
        filmDto.setDuration(90);

        json = objectMapper.writeValueAsString(filmDto);

        String jsonResponse = mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Film responceFilm = objectMapper.readValue(jsonResponse, Film.class);

        assertEquals(filmDto.getDuration(), responceFilm.getDuration());
    }

    @Test
    @DirtiesContext
    void test_putUpdateDuplicateFilmShouldReturn400() throws Exception {
        Film film1 = new Film();
        film1.setName("name");
        film1.setDescription("description");
        film1.setReleaseDate(LocalDate.now().minusYears(1));
        film1.setDuration(100);

        Film film2 = new Film();
        film2.setName("name2");
        film2.setDescription("description2");
        film2.setReleaseDate(LocalDate.now().minusYears(2));
        film2.setDuration(90);

        String json = objectMapper.writeValueAsString(film1);

        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        json = objectMapper.writeValueAsString(film2);

        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        FilmUpdateDto filmDto = new FilmUpdateDto();
        filmDto.setId(1L);
        filmDto.setName("name2");
        filmDto.setDescription("description2");
        filmDto.setReleaseDate(LocalDate.now().minusYears(2));
        filmDto.setDuration(90);

        json = objectMapper.writeValueAsString(filmDto);

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(400));
    }

    @Test
    @DirtiesContext
    void test_putWithMissingIdShouldReturn404() throws Exception {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.now().minusYears(1));
        film.setDuration(100);

        String json = objectMapper.writeValueAsString(film);

        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        FilmUpdateDto filmDto = new FilmUpdateDto();
        filmDto.setId(2L);
        filmDto.setDuration(90);

        json = objectMapper.writeValueAsString(filmDto);

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(404));
    }
}
