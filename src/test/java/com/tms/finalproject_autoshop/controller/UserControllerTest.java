package com.tms.finalproject_autoshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.finalproject_autoshop.model.User;
import com.tms.finalproject_autoshop.model.dto.UserCreateDto;
import com.tms.finalproject_autoshop.model.dto.UserUpdateDto;
import com.tms.finalproject_autoshop.security.CustomUserDetailService;
import com.tms.finalproject_autoshop.security.JwtAuthFilter;
import com.tms.finalproject_autoshop.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    UserService userService;

    @MockitoBean
    JwtAuthFilter jwtFilter;

    static List<User> users = new ArrayList<>();
    static User user = new User();
    static UserCreateDto userCreateDto = new UserCreateDto();
    static UserUpdateDto userUpdateDto = new UserUpdateDto();

    static MockedStatic<CustomUserDetailService> customUserMock;

    @BeforeAll
    static void init() {
        user.setId(5L);
        users.add(user);

        userCreateDto.setEmail("test@mail.com");
        userCreateDto.setAge(18);
        userCreateDto.setFirstName("Test");
        userCreateDto.setSecondName("Testovich");

        customUserMock = Mockito.mockStatic(CustomUserDetailService.class);
        customUserMock.when(CustomUserDetailService::getUserId).thenReturn(5L);
    }

    @AfterAll
    static void cleanup() {
        customUserMock.close();
    }

    // GET ALL USERS
    @Test
    void getAllUsers_Success() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(5)));
    }

    @Test
    void getAllUsers_Empty() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/user"))
                .andExpect(status().isNotFound());
    }

    // CREATE USER
    @Test
    void createUser_Created() throws Exception {
        Mockito.when(userService.createUser(any())).thenReturn(true);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createUser_Conflict() throws Exception {
        Mockito.when(userService.createUser(any())).thenReturn(false);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDto)))
                .andExpect(status().isBadRequest());
    }

    // GET USER BY ID
    @Test
    void getUserById_Found() throws Exception {
        Mockito.when(userService.getUserById(5L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/user/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(5)));
    }

    @Test
    void getUserById_NotFound() throws Exception {
        Mockito.when(userService.getUserById(5L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/user/5"))
                .andExpect(status().isNotFound());
    }

    // UPDATE USER
    @Test
    void updateUser_Success() throws Exception {
        Mockito.when(userService.updateUser(any(), anyLong())).thenReturn(true);

        mockMvc.perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_NotFound() throws Exception {
        Mockito.when(userService.updateUser(any(), anyLong())).thenReturn(false);

        mockMvc.perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andExpect(status().isNotFound());
    }

    // DELETE USER
    @Test
    void deleteUser_NoContent() throws Exception {
        Mockito.when(userService.deleteUser(5L)).thenReturn(true);

        mockMvc.perform(delete("/user/5"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_NotFound() throws Exception {
        Mockito.when(userService.deleteUser(5L)).thenReturn(false);

        mockMvc.perform(delete("/user/5"))
                .andExpect(status().isNotFound());
    }
}
