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
import static org.mockito.BDDMockito.given;
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

    UserModel user1 = new UserModel("hadi");
    UserModel user2 = new UserModel("oumar");
    UserModel user3 = new UserModel("saliou");
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
    public void updateUserTest() throws Exception{
        given(userController.updateUser(user1.getId(), user1.getName())).willReturn(new ResponseEntity(HttpStatus.CREATED));

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

}