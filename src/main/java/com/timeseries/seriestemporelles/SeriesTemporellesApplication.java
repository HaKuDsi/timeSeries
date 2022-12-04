package com.timeseries.seriestemporelles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@SpringBootApplication
@EnableCaching
public class SeriesTemporellesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeriesTemporellesApplication.class, args);
    }

}
