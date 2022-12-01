package com.timeseries.seriestemporelles.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timeseries.seriestemporelles.model.UserModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserController userController;

    UserModel user1 = new UserModel(1, "hadi");
    UserModel user2 = new UserModel(2, "oumar");
    UserModel user3 = new UserModel(3, "saliou");
    List<UserModel> allUsers = new ArrayList<>(Arrays.asList(user1, user2, user3));

    @Test
    public void getAllUsersTest() throws Exception {

        given(userController.getAllUsers()).willReturn(allUsers);

        mvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("[1].name", is(user2.getName())));
    }

    @Test
    public void getUserByIdTest() throws Exception {
        given(userController.getUserById(user1.getId())).willReturn(user1);

        mvc.perform(get("/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("name", is(user1.getName())));
    }

    @Test
    public void getUserByIdTestFail() throws Exception {
        given(userController.getUserById(10)).willReturn(null);

        mvc.perform(get("/user/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void createUserTest() throws Exception {

        given(userController.createUser(user1.getName())).willReturn(new ResponseEntity(HttpStatus.CREATED));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/user")
                .param("userName", user1.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(mockRequest)
                .andExpect(status().isCreated());
    }

    @Test
    public void createUserWithoutParamTest() throws Exception {

        given(userController.createUser(user1.getName())).willReturn(new ResponseEntity(HttpStatus.CREATED));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(mockRequest)
                .andExpect(status().isBadRequest());
    }
/*
    @Test
    public void updateUserTest() throws Exception{
        given(userController.updateUser(user1.getId(), user2.getName())).willReturn(new ResponseEntity(HttpStatus.OK));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/user/1")
                .param("userName", user2.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

 */

    @Test
    public void deleteByIdTest() throws Exception {
        given(userController.deleteById(user1.getId())).willReturn(new ResponseEntity(HttpStatus.OK));

        mvc.perform(delete("/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}