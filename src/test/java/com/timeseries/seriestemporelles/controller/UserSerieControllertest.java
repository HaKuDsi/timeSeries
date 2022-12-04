package com.timeseries.seriestemporelles.controller;

import com.timeseries.seriestemporelles.exception.ResourceNotFoundException;
import com.timeseries.seriestemporelles.model.*;
import com.timeseries.seriestemporelles.service.SeriesService;
import com.timeseries.seriestemporelles.service.UserSerieService;
import com.timeseries.seriestemporelles.service.UserService;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserSerieControllertest {
    @Mock
    UserSerieService userSerieService;

    @InjectMocks
    UserSerieController userSerieController;

    @Mock
    UserService userService;

    @Mock
    SeriesService seriesService;

    UserModel user1 = new UserModel(1, "hadi");
    UserModel user2 = new UserModel(2, "oumar");
    SeriesModel serie1 = new SeriesModel(3, "running", "i like to run");
    SeriesModel serie2 = new SeriesModel(4, "running", "i like to run");
    UserSeriesModel userSeries = new UserSeriesModel(5, user1, serie1, UserPrivilage.WRITE_PRIVILAGE, true);
    UserSeriesModel userSerie2 = new UserSeriesModel(6, user2, serie2, UserPrivilage.READ_PRIVILAGE, false);

    @Test
    public void getAllUserSeriesTest() {
        var userSeriesList = new UserSeriesModel[]{
                userSeries,
                userSerie2
        };
        when(userSerieService.getAllUserSeries()).thenReturn(List.of(userSeriesList));
        var response = userSerieController.getAllUserSeries();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody())
                .extracting(f -> Tuple.tuple(f.getUsers(), f.getSeries(), f.getOwner()))
                .containsExactly(
                        Tuple.tuple(userSeries.getUsers(), userSeries.getSeries(), userSeries.getOwner()),
                        Tuple.tuple(userSerie2.getUsers(), userSerie2.getSeries(), userSerie2.getOwner())
                );
        verify(userSerieService).getAllUserSeries();
    }

    @Test
    public void shareSerieTest_write() {
        when(userService.getUserById(user1.getId())).thenReturn(Optional.ofNullable(user1));
        when(userService.getUserById(user2.getId())).thenReturn(Optional.ofNullable(user2));
        when(seriesService.getSerieById(serie1.getId())).thenReturn(Optional.ofNullable(serie1));
        when(userSerieService.getUserSerieByUserSerie(user1, serie1)).thenReturn(Optional.ofNullable(userSeries));

        var response = userSerieController.shareSerie(serie1.getId(),
                user1.getId(),
                user2.getId(),
                true);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void shareSerieTest_read() {
        when(userService.getUserById(user1.getId())).thenReturn(Optional.ofNullable(user1));
        when(userService.getUserById(user2.getId())).thenReturn(Optional.ofNullable(user2));
        when(seriesService.getSerieById(serie1.getId())).thenReturn(Optional.ofNullable(serie1));
        when(userSerieService.getUserSerieByUserSerie(user1, serie1)).thenReturn(Optional.ofNullable(userSeries));

        var response = userSerieController.shareSerie(serie1.getId(),
                user1.getId(),
                user2.getId(),
                false);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void shareSerieTest_noFromUser() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userSerieController.shareSerie(serie1.getId(), anyInt(), user2.getId(), true));
        assertTrue(exception.getMessage().contains("User: 0 is not found."));
    }

    @Test
    public void shareSerieTest_noToUser() {
        when(userService.getUserById(user1.getId())).thenReturn(Optional.ofNullable(user1));
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userSerieController.shareSerie(serie1.getId(), user1.getId(), anyInt(), true));
        assertTrue(exception.getMessage().contains("User: 0 is not found."));
    }

    @Test
    public void shareSerieTest_noSerie() {
        when(userService.getUserById(user1.getId())).thenReturn(Optional.ofNullable(user1));
        when(userService.getUserById(user2.getId())).thenReturn(Optional.ofNullable(user2));
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userSerieController.shareSerie(anyInt(), user1.getId(), user2.getId(), true));
        assertTrue(exception.getMessage().contains("Serie: 0 is not found."));
    }

    @Test
    public void shareSerieTest_noUserSerie() {
        when(userService.getUserById(user1.getId())).thenReturn(Optional.ofNullable(user1));
        when(userService.getUserById(user2.getId())).thenReturn(Optional.ofNullable(user2));
        when(seriesService.getSerieById(serie1.getId())).thenReturn(Optional.ofNullable(serie1));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userSerieController.shareSerie(serie1.getId(), user1.getId(), user2.getId(), true));
        assertTrue(exception.getMessage().contains("UserSerie is not found."));
    }

    @Test
    public void shareSerieTest_noPrivilage() {
        when(userService.getUserById(user1.getId())).thenReturn(Optional.ofNullable(user1));
        when(userService.getUserById(user2.getId())).thenReturn(Optional.ofNullable(user2));
        when(seriesService.getSerieById(serie2.getId())).thenReturn(Optional.ofNullable(serie2));
        when(userSerieService.getUserSerieByUserSerie(user2, serie2)).thenReturn(Optional.ofNullable(userSerie2));

        var response = userSerieController.shareSerie(serie2.getId(),
                user2.getId(),
                user1.getId(),
                true);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().toString().contains("User doesn't have permission"));
    }

    @Test
    public void shareSerieTest_fail() {
        var response = userSerieController.shareSerie(serie1.getId(),
                user1.getId(),
                user2.getId(),
                null);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
