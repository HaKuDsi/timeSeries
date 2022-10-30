package com.timeseries.seriestemporelles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@SpringBootApplication
public class SeriesTemporellesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeriesTemporellesApplication.class, args);
        ZonedDateTime lastUpdatedDate = ZonedDateTime.now(ZoneId.of("UTC"));
        String date = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm z", Locale.FRANCE).format(lastUpdatedDate);
        System.out.println(date);
    }

}
