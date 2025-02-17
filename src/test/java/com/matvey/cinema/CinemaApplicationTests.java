package com.matvey.cinema;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CinemaApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetWithQueryParams() throws Exception {
        mockMvc.perform(get("/api/query?title=Inception&director=Nolan&releaseYear=2010&genre=Sci-Fi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception"))
                .andExpect(jsonPath("$.director").value("Nolan"))
                .andExpect(jsonPath("$.releaseYear").value(2010))
                .andExpect(jsonPath("$.genre").value("Sci-Fi"));
    }

    @Test
    void testGetWithPathParams() throws Exception {
        mockMvc.perform(get("/api/path/Inception/Nolan/2010/Sci-Fi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception"))
                .andExpect(jsonPath("$.director").value("Nolan"))
                .andExpect(jsonPath("$.releaseYear").value(2010))
                .andExpect(jsonPath("$.genre").value("Sci-Fi"));
    }
}
