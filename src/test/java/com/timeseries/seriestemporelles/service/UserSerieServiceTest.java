package com.timeseries.seriestemporelles.service;

import com.timeseries.seriestemporelles.model.SeriesModel;
import com.timeseries.seriestemporelles.model.UserModel;
import com.timeseries.seriestemporelles.model.UserPrivilage;
import com.timeseries.seriestemporelles.model.UserSeriesModel;
import com.timeseries.seriestemporelles.repository.UserSerieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserSerieServiceTest {

    @Mock
    UserSerieRepository userSerieRepository;

    @InjectMocks
    UserSerieService userSerieService;

    UserModel user = new UserModel(1, "hadi");
    SeriesModel serie = new SeriesModel(2, "run", "i like running");
    UserSeriesModel userSerie = new UserSeriesModel(3, user, serie, UserPrivilage.WRITE_PRIVILAGE, true);

    UserModel user2 = new UserModel(4, "hadi");
    SeriesModel serie2 = new SeriesModel(5, "run", "i like running");
    UserSeriesModel userSerie2 = new UserSeriesModel(6, user, serie, UserPrivilage.WRITE_PRIVILAGE, true);

    @Test
    public void getAllUserSeriesTest() {
        var userSeries = new UserSeriesModel[]{
                userSerie,
                userSerie2
        };
        when(userSerieRepository.findAll()).thenReturn(Arrays.asList(userSeries));

        var response = userSerieService.getAllUserSeries();

        assertThat(response).isNotNull();
        assertThat(response).contains(userSerie);
    }

    @Test
    public void getUserSerieByIdTest() {
        when(userSerieRepository.findById(userSerie.getId())).thenReturn(Optional.ofNullable(userSerie));

        var response = userSerieService.getUserSerieById(userSerie.getId());

        assertThat(response).isNotNull();
        assertThat(response).contains(userSerie);
    }

    @Test
    public void saveOrUpdateSerieTest() {
        userSerieService.saveOrUpdateUserSerie(userSerie);

        verify(userSerieRepository).save(userSerie);
    }

    @Test
    public void deleteUserSerieTest() {
        userSerieService.deleteUserSerie(userSerie.getId());

        verify(userSerieRepository).deleteById(userSerie.getId());
    }

    @Test
    public void getUserSerieByUserSerieTest() {
        when(userSerieRepository.userSerieExist(user, serie)).thenReturn(Optional.ofNullable(userSerie));

        var response = userSerieService.getUserSerieByUserSerie(user, serie);

        assertThat(response).isNotNull();
        assertThat(response).contains(userSerie);
    }

    @Test
    public void getAllUserSeriesBySerieTest() {
        UserSeriesModel userserie3 = new UserSeriesModel(6, user2, serie, UserPrivilage.READ_PRIVILAGE, false);
        var userseries = new UserSeriesModel[]{
                userSerie,
                userserie3
        };

        when(userSerieRepository.allUserSeriesFromSerie(serie)).thenReturn(Arrays.asList(userseries));

        var response = userSerieService.getAllUserSeriesBySerie(serie);

        assertThat(response).isNotNull();
        assertThat(response).contains(userSerie);
    }
}
