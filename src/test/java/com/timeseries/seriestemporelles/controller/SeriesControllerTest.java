package com.timeseries.seriestemporelles.controller;

import com.timeseries.seriestemporelles.exception.ResourceNotFoundException;
import com.timeseries.seriestemporelles.model.SeriesModel;
import com.timeseries.seriestemporelles.model.UserModel;
import com.timeseries.seriestemporelles.model.UserPrivilage;
import com.timeseries.seriestemporelles.model.UserSeriesModel;
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

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SeriesControllerTest {

    @Mock
    SeriesService seriesService;

    @InjectMocks
    SeriesController seriesController;

    @Mock
    UserService userService;

    @Mock
    UserSerieService userSerieService;

    SeriesModel serie = new SeriesModel(1, "runnung", "i like to run");
    UserModel user = new UserModel(3, "hadi");

    @Test
    public void getAllSeriesTest() throws Exception {
        var series = new SeriesModel[]{
                new SeriesModel(1, "runnung", "i like to run"),
                new SeriesModel(2, "swimming", "i like to swim")
        };

        when(seriesService.getAllSeries()).thenReturn(Arrays.asList(series));

        var response = seriesController.getAllSeries();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody())
                .extracting(f -> Tuple.tuple(f.getId(), f.getTitle(), f.getDescription()))
                .containsExactly(
                        Tuple.tuple(1,"runnung","i like to run" ),
                        Tuple.tuple(2, "swimming", "i like to swim")
                );
        verify(seriesService).getAllSeries();
    }

    @Test
    public void getSerieByIdTest() throws Exception {
        UserModel user = new UserModel(3, "hadi");
        UserSeriesModel userSeries = new UserSeriesModel(5, user, serie, UserPrivilage.WRITE_PRIVILAGE, true);

        when(seriesService.getSerieById(serie.getId())).thenReturn(Optional.ofNullable(serie));
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));
        when(userSerieService.getUserSerieByUserSerie(user, serie)).thenReturn(Optional.of(userSeries));

        var response = seriesController.getSerieById(serie.getId(), user.getId());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(serie);
        verify(seriesService).getSerieById(serie.getId());
    }

    @Test
    public void getSerieByIdTest_noUser() {
        ResourceNotFoundException exeption = assertThrows(ResourceNotFoundException.class,
                () -> seriesController.getSerieById(serie.getId(), anyInt()));

        assertTrue(exeption.getMessage().contains("User: 0 not found."));
        verify(userService).getUserById(anyInt());
    }

    @Test
    public void getSerieByIdTest_noSerie() {
        UserModel user = new UserModel(3, "hadi");
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));

        ResourceNotFoundException exeption = assertThrows(ResourceNotFoundException.class,
                () -> seriesController.getSerieById(anyInt(), user.getId()));

        assertTrue(exeption.getMessage().contains("Serie: 0 not found."));
        verify(seriesService).getSerieById(anyInt());
    }
    @Test
    public void getSerieByIdTest_noUserSerie() {
        UserModel user = new UserModel(3, "hadi");
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));
        when(seriesService.getSerieById(serie.getId())).thenReturn(Optional.ofNullable(serie));

        ResourceNotFoundException exeption = assertThrows(ResourceNotFoundException.class,
                () -> seriesController.getSerieById(serie.getId(), user.getId()));

        assertTrue(exeption.getMessage().contains("User: 3 cannot see serie: 1."));
        verify(userSerieService).getUserSerieByUserSerie(user, serie);
    }

    @Test
    public void getSerieByIdTest_noPrivilage() throws Exception {
        UserSeriesModel userSeries = new UserSeriesModel(5, user, serie, UserPrivilage.READ_PRIVILAGE, true);

        when(seriesService.getSerieById(serie.getId())).thenReturn(Optional.ofNullable(serie));
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));
        when(userSerieService.getUserSerieByUserSerie(user, serie)).thenReturn(Optional.of(userSeries));

        var response = seriesController.getSerieById(serie.getId(), user.getId());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
        verify(seriesService).getSerieById(serie.getId());
    }

    @Test
    public void createSerieTest() {
        doNothing().when(seriesService).saveOrUpdate(serie);
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));

        var response = seriesController.createSerie(user.getId(), serie);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        verify(seriesService).saveOrUpdate(serie);
    }

    @Test
    public void createSerieTest_noUser() {
        ResourceNotFoundException exeption = assertThrows(ResourceNotFoundException.class,
                () -> seriesController.createSerie(anyInt(), serie));

        assertTrue(exeption.getMessage().contains("User: 0 not found."));
        verify(userService).getUserById(anyInt());
    }

    @Test
    public void createSerieTest_fail() {
        var response = seriesController.createSerie(null, null);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void updateSerieTest() {
        UserSeriesModel userSeries = new UserSeriesModel(5, user, serie, UserPrivilage.WRITE_PRIVILAGE, true);

        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));
        when(seriesService.getSerieById(serie.getId())).thenReturn(Optional.ofNullable(serie));
        when(userSerieService.getUserSerieByUserSerie(user, serie)).thenReturn(Optional.of(userSeries));

        var response = seriesController.updateSerie(serie.getId(),
                user.getId(),
                serie.getTitle(),
                serie.getDescription());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        verify(seriesService).saveOrUpdate(serie);
    }

    @Test
    public void updateSerieTest_noUser() {
        ResourceNotFoundException exeption = assertThrows(ResourceNotFoundException.class,
                () -> seriesController.updateSerie(serie.getId(), anyInt(), serie.getTitle(), serie.getDescription()));

        assertTrue(exeption.getMessage().contains("User: 0 not found."));
    }

    @Test
    public void updateSerieTest_noUserSerie() {
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));
        when(seriesService.getSerieById(serie.getId())).thenReturn(Optional.ofNullable(serie));

        ResourceNotFoundException exeption = assertThrows(ResourceNotFoundException.class,
                () -> seriesController.updateSerie(serie.getId(), user.getId(), serie.getTitle(), serie.getDescription()));

        assertTrue(exeption.getMessage().contains("UserSerie is not found."));
    }

    @Test
    public void updateSerieTest_noSerie() {
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));
        ResourceNotFoundException exeption = assertThrows(ResourceNotFoundException.class,
                () -> seriesController.updateSerie(anyInt(), user.getId(), serie.getTitle(), serie.getDescription()));

        assertTrue(exeption.getMessage().contains("Serie: 0 not found."));
        verify(userService).getUserById(anyInt());
    }

    @Test
    public void updateSerieTest_noPrivilage() {
        UserSeriesModel userSeries = new UserSeriesModel(5, user, serie, UserPrivilage.WRITE_PRIVILAGE, false);

        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));
        when(seriesService.getSerieById(serie.getId())).thenReturn(Optional.ofNullable(serie));
        when(userSerieService.getUserSerieByUserSerie(user, serie)).thenReturn(Optional.of(userSeries));

        var response = seriesController.updateSerie(serie.getId(),
                user.getId(),
                serie.getTitle(),
                serie.getDescription());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void updateSerieTest_fail() {
        var response = seriesController.updateSerie(serie.getId(), user.getId(), null, null);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deleteSerieTest() {
        UserSeriesModel userSeries = new UserSeriesModel(5, user, serie, UserPrivilage.WRITE_PRIVILAGE, true);

        doNothing().when(seriesService).delete(serie.getId());
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));
        when(seriesService.getSerieById(serie.getId())).thenReturn(Optional.ofNullable(serie));
        when(userSerieService.getUserSerieByUserSerie(user, serie)).thenReturn(Optional.of(userSeries));

        var response = seriesController.deleteSerieById(serie.getId(), user.getId());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        verify(seriesService).delete(serie.getId());
    }

    @Test
    public void deleteSerieTest_noUser() {
        var response = seriesController.deleteSerieById(serie.getId(), anyInt());

        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void deleteSerieTest_noSerie() {
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));

        var response = seriesController.deleteSerieById(anyInt(), user.getId());

        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Test
    public void deleteSerieTest_noUserSerie() {
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));
        when(seriesService.getSerieById(serie.getId())).thenReturn(Optional.ofNullable(serie));
        var response = seriesController.deleteSerieById(serie.getId(), user.getId());

        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void deleteSerieTest_noPrivilage() {
        UserSeriesModel userSeries = new UserSeriesModel(5, user, serie, UserPrivilage.WRITE_PRIVILAGE, false);

        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));
        when(seriesService.getSerieById(serie.getId())).thenReturn(Optional.ofNullable(serie));
        when(userSerieService.getUserSerieByUserSerie(user, serie)).thenReturn(Optional.of(userSeries));

        var response = seriesController.deleteSerieById(serie.getId(), user.getId());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
    }
}

