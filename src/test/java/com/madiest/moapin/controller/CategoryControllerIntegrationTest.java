package com.madiest.moapin.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madiest.moapin.auth.dto.LoginRequest;
import com.madiest.moapin.auth.dto.SignUpRequest;
import java.util.Arrays;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.parallel.Isolated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(
    properties = {
      "spring.datasource.url=jdbc:h2:mem:testdb",
      "spring.datasource.driver-class-name=org.h2.Driver",
      "app.jwt.secret=dGhpcy1pcy1hLXZlcnktbG9uZy1hbmQtc2VjdXJlLXNlY3JldC1rZXktZm9yLWp3dC10ZXN0aW5nCg==",
      "app.storage.endpoint=http://localhost:9000",
      "app.storage.access-key=dummy",
      "app.storage.secret-key=dummy",
      "app.storage.bucket=test",
      "app.search.host=http://localhost:7700",
      "app.search.api-key=dummy",
      "app.email.access-key=dummy",
      "app.email.secret-key=dummy",
      "app.email.region=us-east-1",
      "spring.test.aot.enabled=false"
    })
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestMethodOrder(MethodOrderer.Random.class)
@Isolated
public class CategoryControllerIntegrationTest {

  @Autowired private MockMvc mvc;

  @Autowired private ObjectMapper mapper;

  private String token;
  private String testPrefix;

  @BeforeEach
  void setUp() throws Exception {
    testPrefix = UUID.randomUUID().toString().substring(0, 8);
    
    SignUpRequest signup = new SignUpRequest();
    signup.setUsername("catuser");
    signup.setEmail("cat@example.com");
    signup.setPassword("pass1234");
    mvc.perform(
            post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signup)))
        .andExpect(status().isOk());

    LoginRequest login = new LoginRequest();
    login.setUsername("catuser");
    login.setPassword("pass1234");
    String resp =
        mvc.perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(login)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    token = mapper.readValue(resp, JsonNode.class).get("accessToken").asText();
  }

  @Test
  void testUpdateCategoryName() throws Exception {
    // create category
    String create =
        mvc.perform(
                post("/api/categories")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"" + testPrefix + "_Original\"}"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    Long id = mapper.readTree(create).get("id").asLong();

    // update name
    mvc.perform(
            put("/api/categories/" + id)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"" + testPrefix + "_Updated\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(testPrefix + "_Updated"));
  }

  @Test
  void testReorderCategories() throws Exception {
    // create two categories with unique names
    String c1 =
        mvc.perform(
                post("/api/categories")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"" + testPrefix + "_A\"}"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    long id1 = mapper.readTree(c1).get("id").asLong();
    String c2 =
        mvc.perform(
                post("/api/categories")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"" + testPrefix + "_B\"}"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    long id2 = mapper.readTree(c2).get("id").asLong();

    // reorder: [id2, id1]
    mvc.perform(
            patch("/api/categories/reorder")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Arrays.asList(id2, id1))))
        .andExpect(status().isNoContent());

    // verify order via sort=custom
    String list =
        mvc.perform(get("/api/categories?sort=custom").header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    JsonNode arr = mapper.readTree(list);
    assertThat(arr.get(0).get("id").asLong()).isEqualTo(id2);
    assertThat(arr.get(1).get("id").asLong()).isEqualTo(id1);
  }

  @Test
  void testDeleteCategory() throws Exception {
    String create =
        mvc.perform(
                post("/api/categories")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"" + testPrefix + "_ToDelete\"}"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    long id = mapper.readTree(create).get("id").asLong();

    mvc.perform(delete("/api/categories/" + id).header("Authorization", "Bearer " + token))
        .andExpect(status().isNoContent());

    mvc.perform(get("/api/categories/" + id).header("Authorization", "Bearer " + token))
        .andExpect(status().isNotFound());
  }

  @Test
  void testSortByNameAndCreatedDate() throws Exception {
    // Create categories with distinct names and timestamps
    mvc.perform(
            post("/api/categories")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"" + testPrefix + "_B\"}"))
        .andExpect(status().isOk());
    Thread.sleep(10);
    mvc.perform(
            post("/api/categories")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"" + testPrefix + "_A\"}"))
        .andExpect(status().isOk());

    // sort by name (ascending - A should come first)
    String byName =
        mvc.perform(get("/api/categories?sort=name&direction=ASC").header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    JsonNode arrName = mapper.readTree(byName);
    assertThat(arrName.get(0).get("name").asText()).isEqualTo(testPrefix + "_A");

    // sort by createdDate (default DESC - B should come first as it was created first)
    String byDate =
        mvc.perform(
                get("/api/categories?sort=createdDate").header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    JsonNode arrDate = mapper.readTree(byDate);
    assertThat(arrDate.get(0).get("name").asText()).isEqualTo(testPrefix + "_A");
  }

  @Test
  void testAccessDeniedForOtherUser() throws Exception {
    // Create fresh user and token for this test
    String user1Prefix = UUID.randomUUID().toString().substring(0, 8);
    SignUpRequest signup1 = new SignUpRequest();
    signup1.setUsername(user1Prefix + "_user1");
    signup1.setEmail(user1Prefix + "_user1@example.com");
    signup1.setPassword("pass1234");
    mvc.perform(
            post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signup1)))
        .andExpect(status().isOk());
    
    LoginRequest login1 = new LoginRequest();
    login1.setUsername(user1Prefix + "_user1");
    login1.setPassword("pass1234");
    String resp1 =
        mvc.perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(login1)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    String token1 = mapper.readTree(resp1).get("accessToken").asText();

    // user1 creates a category
    String create =
        mvc.perform(
                post("/api/categories")
                    .header("Authorization", "Bearer " + token1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"" + user1Prefix + "_Secret\"}"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    long id = mapper.readTree(create).get("id").asLong();

    // register and login user2 with unique credentials
    String otherTestPrefix = UUID.randomUUID().toString().substring(0, 8);
    SignUpRequest signup2 = new SignUpRequest();
    signup2.setUsername(otherTestPrefix + "_other");
    signup2.setEmail(otherTestPrefix + "_other@example.com");
    signup2.setPassword("pass1234");
    mvc.perform(
            post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signup2)))
        .andExpect(status().isOk());
    LoginRequest login2 = new LoginRequest();
    login2.setUsername(otherTestPrefix + "_other");
    login2.setPassword("pass1234");
    String resp2 =
        mvc.perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(login2)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    String token2 = mapper.readTree(resp2).get("accessToken").asText();

    // user2 should not be able to modify user1's category
    mvc.perform(
            put("/api/categories/" + id)
                .header("Authorization", "Bearer " + token2)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"X\"}"))
        .andExpect(status().isForbidden());
    mvc.perform(delete("/api/categories/" + id).header("Authorization", "Bearer " + token2))
        .andExpect(status().isForbidden());
  }
}
