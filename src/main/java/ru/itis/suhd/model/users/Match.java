package ru.itis.suhd.model.users;

import com.opencsv.bean.CsvBindByPosition;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "match")
public class Match {

    @Id
    @CsvBindByPosition(position = 0)
    private String id;

    @CsvBindByPosition(position = 4)
    private String homeTeam;

    @CsvBindByPosition(position = 5)
    private String awayTeam;

    @CsvBindByPosition(position = 2)
    private String startTime;

    private String weekday;

    @CsvBindByPosition(position = 7)
    private String result;
}
