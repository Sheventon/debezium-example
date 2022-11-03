package ru.itis.suhd.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.debezium.data.Envelope.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.suhd.kafka.model.WatchLog;
import ru.itis.suhd.model.stats.UserMatchStat;
import ru.itis.suhd.repository.stats.UserMatchRepository;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DwhServiceImpl implements DwhService {

    private final UserMatchRepository userMatchRepository;

    @Override
    public void replicateData(Map<String, Object> objectMap, Operation operation) {
        final ObjectMapper mapper = new ObjectMapper();
        final WatchLog watchLog = mapper.convertValue(objectMap, WatchLog.class);

        Optional<UserMatchStat> optionalUserMatchStat = userMatchRepository.findByDate(watchLog.getDate());
        if (optionalUserMatchStat.isEmpty()) {
            userMatchRepository.save(UserMatchStat.builder()
                    .totalTime(watchLog.getTotalTime())
                    .weekday(watchLog.getWeekday())
                    .date(watchLog.getDate())
                    .isMatchDay(watchLog.isMatchDay())
                    .matches(watchLog.getMatches())
                    .build());
        } else {
            UserMatchStat userMatchStat = optionalUserMatchStat.get();
            userMatchStat.setTotalTime(userMatchStat.getTotalTime() + watchLog.getTotalTime());
            userMatchRepository.save(userMatchStat);
        }
    }
}
