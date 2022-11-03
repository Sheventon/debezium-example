package ru.itis.suhd.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WatchLog {

    private Long userId;

    private String weekday;

    private LocalDate date;

    private boolean isMatchDay;

    private int totalTime;

    private String matches;

    private String mostViewsCountry;
}
