package com.timeseries.seriestemporelles.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timeseries.seriestemporelles.exception.ResourceNotFoundException;
import com.timeseries.seriestemporelles.model.UserModel;
import com.timeseries.seriestemporelles.repository.UserRepository;
import com.timeseries.seriestemporelles.service.UserService;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
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

    UserModel user = new UserModel(1, "hadi");

    @Test
    public void getAllUsersTest() {

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

    @Test
    @DisplayName("should return user with the name hadi")
    public void getUserByIdTest() throws Exception {
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));

        var response = userController.getUserById(user.getId());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(user);
        verify(userService).getUserById(user.getId());
    }

    @Test
    @DisplayName("should throw exception: ResourceNotFoundException(\"User: 0 is not found.\")")
    public void getUserByIdTest_fails() throws Exception {
        ResourceNotFoundException exeption = assertThrows(ResourceNotFoundException.class,
                () -> userController.getUserById(anyInt()));

        assertTrue(exeption.getMessage().contains("User: 0 is not found."));
        verify(userService).getUserById(anyInt());
    }

    @Test
    @DisplayName("should create a new user with the name hadi")
    public void createUserTest() throws Exception {
        doNothing().when(userService).saveOrUpdate(user);

        var response = userController.createUser(user);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        verify(userService).saveOrUpdate(user);
    }

    @Test
    public void updateUserTest() throws Exception{

        doNothing().when(userService).saveOrUpdate(user);
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));

        var response = userController.updateUser(user.getId(), user);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo("User modified with id: 1");
        verify(userService).saveOrUpdate(user);
    }

    @Test
    public void updateUserTest_fails() throws Exception {
        ResourceNotFoundException exeption = assertThrows(ResourceNotFoundException.class,
                () -> userController.updateUser(anyInt(), user));

        assertTrue(exeption.getMessage().contains("User: 0 not found."));
        verify(userService).getUserById(1);
    }

    @Test
    public void deleteUserByIdTest() {
        doNothing().when(userService).delete(1);

        var response = userController.deleteUserById(1);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        verify(userService).delete(1);
    }
}