package com.timeseries.seriestemporelles.controller;
/*
import com.timeseries.seriestemporelles.model.SeriesModel;
import com.timeseries.seriestemporelles.model.UserPrivilage;
import com.timeseries.seriestemporelles.model.UserSeriesModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
@WebMvcTest(SeriesController.class)
public class SeriesControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SeriesController seriesController;

    SeriesModel serie1 = new SeriesModel(1, "runnung", "i like to run");
    SeriesModel serie2 = new SeriesModel(1, "swimming", "i like to swim");
    List<SeriesModel> allseries = new ArrayList<>(Arrays.asList(serie1, serie2));

    @Test
    public void getSeriesTest() throws Exception {
        given(seriesController.getAllSeries()).willReturn(allseries);

        mvc.perform(get("/series")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("[1].title", is(serie2.getTitle())));
    }
/*
    @Test
    public void getSerieByIdTest() throws Exception {
        UserModel user = new UserModel(3, "hadi");
        UserSeriesModel userSerie = new UserSeriesModel(4, user, serie1, UserPrivilage.WRITE_PRIVILAGE, true);

        given(seriesController.getSerieById());
    }



}

*/