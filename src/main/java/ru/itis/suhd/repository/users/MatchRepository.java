package ru.itis.suhd.repository.users;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.itis.suhd.model.users.Match;

import java.util.List;

public interface MatchRepository extends MongoRepository<Match, String> {

    List<Match> findByStartTime(String startTime);
}
