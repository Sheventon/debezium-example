package ru.itis.suhd.model.stats;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_match_stat")
public class UserMatchStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String weekday;

    private LocalDate date;

    @Column(name = "total_time")
    private long totalTime;

    @Column(name = "is_match_day")
    private boolean isMatchDay;

    private String matches;

    @Column(name = "most_views_country")
    private String mostViewsCountry;
}
