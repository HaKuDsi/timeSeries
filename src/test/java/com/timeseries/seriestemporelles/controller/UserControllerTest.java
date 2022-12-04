package com.timeseries.seriestemporelles.controller;

import com.timeseries.seriestemporelles.exception.ResourceNotFoundException;
import com.timeseries.seriestemporelles.model.UserModel;
import com.timeseries.seriestemporelles.service.UserService;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
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
    public void getUserByIdTest() {
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
    public void getUserByIdTest_fails() {
        ResourceNotFoundException exeption = assertThrows(ResourceNotFoundException.class,
                () -> userController.getUserById(anyInt()));

        assertTrue(exeption.getMessage().contains("User: 0 is not found."));
        verify(userService).getUserById(anyInt());
    }

    @Test
    @DisplayName("should create a new user with the name hadi")
    public void createUserTest() {
        doNothing().when(userService).saveOrUpdateUser(user);

        var response = userController.createUser(user);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        verify(userService).saveOrUpdateUser(user);
    }

    @Test
    public void createUserTest_fail() {
        var user = userController.createUser(null);
        assertThat(user.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void updateUserTest() {

        doNothing().when(userService).saveOrUpdateUser(user);
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));

        var response = userController.updateUser(user.getId(), user.getName());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo("User modified with id: 1");
        verify(userService).saveOrUpdateUser(user);
    }

    @Test
    public void updateUserTest_noUser() {
        ResourceNotFoundException exeption = assertThrows(ResourceNotFoundException.class,
                () -> userController.updateUser(anyInt(), user.getName()));

        assertTrue(exeption.getMessage().contains("User: 0 not found."));
        verify(userService).getUserById(anyInt());
    }

    @Test
    public void updateUserTest_nullUser() {
        var response = userController.updateUser(user.getId(), null);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deleteUserByIdTest() {
        doNothing().when(userService).deleteUser(1);

        var response = userController.deleteUserById(1);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        verify(userService).deleteUser(user.getId());
    }

    @Test
    public void deleteUserById_fail() {
        var response = userController.deleteUserById(null);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}