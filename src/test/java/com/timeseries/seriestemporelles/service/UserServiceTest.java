package com.timeseries.seriestemporelles.service;

import com.timeseries.seriestemporelles.model.UserModel;
import com.timeseries.seriestemporelles.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    UserModel user1 = new UserModel(1, "hadi");
    UserModel user2 = new UserModel(2, "oumar");

    @Test
    public void getAllUsersTest() {
        var users = new UserModel[] {
                user1,
                user2
        };
        when(userRepository.findAll()).thenReturn(Arrays.asList(users));

        var response = userService.getAllUsers();

        assertThat(response).isNotNull();
        assertThat(response).contains(user1);
    }

    @Test
    public void getUserByIdTest() {
        when(userRepository.findById(user1.getId())).thenReturn(Optional.ofNullable(user1));

        var response = userService.getUserById(user1.getId());

        assertThat(response).isNotNull();
        assertThat(response).contains(user1);
    }

    @Test
    public void saveOrUpdateUserTest() {
        userService.saveOrUpdateUser(user1);

        verify(userRepository).save(user1);
    }

    @Test
    public void saveOrUpfdateUserTest_null() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> userService.saveOrUpdateUser(new UserModel()));
        assertEquals(error.getMessage(), "cannot save a null user");
    }

    @Test
    public void deleteUserTest() {
        userService.deleteUser(user1.getId());

        verify(userRepository).deleteById(user1.getId());
    }

    @Test
    public void deleteUserTest_null() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> userService.deleteUser(null));
        assertEquals(error.getMessage(), "cannot fetch user with null id");
    }
}
