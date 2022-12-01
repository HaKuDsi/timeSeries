package com.timeseries.seriestemporelles.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timeseries.seriestemporelles.model.UserModel;
import com.timeseries.seriestemporelles.repository.UserRepository;
import com.timeseries.seriestemporelles.service.UserService;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {


    @Mock
    UserService userService;
    @InjectMocks
    UserController userController;

/*
    @Test
    public void getAllUsersTest() throws Exception {

        //given(userController.getAllUsers()).willReturn(allUsers);

        mvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("[1].name", is(user2.getName())));
    }

 */
    @Test
    public void mustReturnFeatureFromService() {

        var userModel = new UserModel[]{
                new UserModel(1, "hadi"),
                new UserModel(2, "oumar"),
                new UserModel(3, "saliou")
        };


        when(userService.getAllUsers()).thenReturn(Arrays.asList(userModel));

        var response = userController.getAllUsers();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody())
                .extracting(f -> Tuple.tuple(f.getId(), f.getName()))
                .containsExactly(
                        Tuple.tuple(1,"hadi"),
                        Tuple.tuple(2, "oumar"),
                        Tuple.tuple(3, "saliou")
                );
        verify(userService).getAllUsers();
    }
    /*
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

        given(userController.createUser(user1)).willReturn(new ResponseEntity(HttpStatus.CREATED));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(mockRequest)
                .andExpect(status().isCreated());
    }

    @Test
    public void createUserWithoutNameTest() throws Exception {
        UserModel userEmpty = new UserModel(4,"");
        given(userController.createUser(user1)).willReturn(new ResponseEntity(HttpStatus.CREATED));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(userEmpty);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void updateUserTest() throws Exception{
        given(userController.updateUser(user1.getId(), user2)).willReturn(new ResponseEntity(HttpStatus.OK));

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

    @Test
    public void deleteByIdTest() throws Exception {
        given(userController.deleteById(user1.getId())).willReturn(new ResponseEntity(HttpStatus.OK));

        mvc.perform(delete("/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

     */
}