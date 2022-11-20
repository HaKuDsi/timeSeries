package com.timeseries.seriestemporelles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@SpringBootApplication
public class SeriesTemporellesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeriesTemporellesApplication.class, args);
        ZoneId timeZone = ZoneId.of("UTC");
        ZonedDateTime zonedDateTime = LocalDateTime.parse("2011-12-03T10:15:30").atZone(timeZone);
        System.out.println(zonedDateTime);
    }

}
