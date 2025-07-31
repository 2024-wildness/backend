package com.madiest.moapin.category;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madiest.moapin.auth.payload.LoginRequest;
import com.madiest.moapin.auth.payload.SignUpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for category update and reorder endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerIntegrationTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        SignUpRequest signup = new SignUpRequest();
        signup.setUsername("catuser");
        signup.setEmail("cat@example.com");
        signup.setPassword("pass1234");
        mvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signup)))
            .andExpect(status().isOk());

        LoginRequest login = new LoginRequest();
        login.setUsername("catuser");
        login.setPassword("pass1234");
        String resp = mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(login)))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        token = mapper.readValue(resp, JsonNode.class).get("accessToken").asText();
    }

    @Test
    void testUpdateCategoryName() throws Exception {
        // create category
        String create = mvc.perform(post("/api/categories")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Original\"}"))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Long id = mapper.readTree(create).get("id").asLong();

        // update name
        mvc.perform(put("/api/categories/" + id)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void testReorderCategories() throws Exception {
        // create two categories
        String c1 = mvc.perform(post("/api/categories")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"A\"}"))
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        long id1 = mapper.readTree(c1).get("id").asLong();
        String c2 = mvc.perform(post("/api/categories")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"B\"}"))
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        long id2 = mapper.readTree(c2).get("id").asLong();

        // reorder: [id2, id1]
        mvc.perform(patch("/api/categories/reorder")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Arrays.asList(id2, id1))))
            .andExpect(status().isNoContent());

        // verify order via sort=custom
        String list = mvc.perform(get("/api/categories?sort=custom")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        JsonNode arr = mapper.readTree(list);
        assertThat(arr.get(0).get("id").asLong()).isEqualTo(id2);
        assertThat(arr.get(1).get("id").asLong()).isEqualTo(id1);
    }
}