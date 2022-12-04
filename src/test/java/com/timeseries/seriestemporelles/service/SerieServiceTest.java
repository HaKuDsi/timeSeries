package com.timeseries.seriestemporelles.service;

import com.timeseries.seriestemporelles.model.SeriesModel;
import com.timeseries.seriestemporelles.repository.SeriesRepository;
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
public class SerieServiceTest {

    @Mock
    SeriesRepository seriesRepository;

    @InjectMocks
    SeriesService seriesService;

    SeriesModel serie1 = new SeriesModel(1, "run" , "i like running");
    SeriesModel serie2 = new SeriesModel(2, "swim" , "i like swimming");

    @Test
    public void getAllSeriesTest() {
        var series = new SeriesModel[]{
                serie1,
                serie2
        };

        when(seriesRepository.findAll()).thenReturn(Arrays.asList(series));

        var response = seriesService.getAllSeries();

        assertThat(response).isNotNull();
        assertThat(response).contains(serie1);
        verify(seriesRepository).findAll();
    }

    @Test
    public void gerSerieByIdTest() {
        when(seriesRepository.findById(serie1.getId())).thenReturn(Optional.ofNullable(serie1));

        var response = seriesService.getSerieById(serie1.getId());

        assertThat(response).isNotNull();
        assertThat(response).contains(serie1);
        verify(seriesRepository).findById(serie1.getId());
    }

    @Test
    public void saveOrUpdateSerieTest() {
        seriesService.saveOrUpdateSerie(serie1);

        verify(seriesRepository).save(serie1);
    }

    @Test
    public void saveOrUpdateSerieTest_nullTitle() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> seriesService.saveOrUpdateSerie(new SeriesModel()));
        assertEquals(error.getMessage(), "title cannot be empty");
    }

    @Test
    public void saveOrUpdateSerieTest_null() {
        SeriesModel seriesModel = new SeriesModel(3, "hump", null);
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> seriesService.saveOrUpdateSerie(seriesModel));
        assertEquals(error.getMessage(), "description cannot be null");
    }

    @Test
    public void deleteSerieByIdTest() {
        seriesService.deleteSerieById(serie1.getId());

        verify(seriesRepository).deleteById(serie1.getId());
    }
}
