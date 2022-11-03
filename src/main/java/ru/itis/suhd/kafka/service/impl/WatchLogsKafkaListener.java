package ru.itis.suhd.kafka.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.itis.suhd.kafka.model.WatchLog;
import ru.itis.suhd.kafka.service.KafkaMessageListener;
import ru.itis.suhd.model.stats.UserMatchStat;
import ru.itis.suhd.repository.stats.UserMatchRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WatchLogsKafkaListener implements KafkaMessageListener {

    private final UserMatchRepository userMatchRepository;

    @Override
    @KafkaListener(topics = "watchLogsTopic", groupId = "watchLogs")
    public void onMessage(WatchLog watchLog) {
        Optional<UserMatchStat> optionalUserMatchStat = userMatchRepository.findByDate(watchLog.getDate());
        if (optionalUserMatchStat.isEmpty()) {
            userMatchRepository.save(UserMatchStat.builder()
                    .totalTime(watchLog.getTotalTime())
                    .weekday(watchLog.getWeekday())
                    .date(watchLog.getDate())
                    .isMatchDay(watchLog.isMatchDay())
                    .matches(watchLog.getMatches())
                    .mostViewsCountry(watchLog.getMostViewsCountry())
                    .build());
        } else {
            UserMatchStat userMatchStat = optionalUserMatchStat.get();
            userMatchStat.setTotalTime(userMatchStat.getTotalTime() + watchLog.getTotalTime());
            userMatchRepository.save(userMatchStat);
        }
    }
}
