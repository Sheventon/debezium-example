package ru.itis.suhd.service;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import ru.itis.suhd.kafka.model.*;
import ru.itis.suhd.kafka.service.KafkaMessageService;
import ru.itis.suhd.model.users.Match;
import ru.itis.suhd.model.users.User;
import ru.itis.suhd.repository.users.MatchRepository;
import ru.itis.suhd.repository.users.UserRepository;
import ru.itis.suhd.util.CsvParser;
import ru.itis.suhd.util.WeekDayMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataGenerateServiceImpl implements DataGenerateService {

    private static int usersCount;

    private final UserRepository userRepository;
    private final MatchRepository matchRepository;

    private final MongoTemplate mongoTemplate;

    private final KafkaMessageService kafkaMessageService;

    private final Faker faker = new Faker();


    @Override
    public void generatePostgresData(int count) {
        usersCount = count;
        for (long i = 1; i <= count; i++) {
            userRepository.save(User.builder()
                    .id(i)
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .email(faker.internet().emailAddress())
                    .country(faker.country().name())
                    .build());
        }
    }

    @Override
    public void generateMongoData() {
        matchRepository.saveAll(getMatchDataFromCsv());
    }

    @Override
    public void generateKafkaLogs() {
        Random random = new Random();
        LocalDate startPeriod = LocalDate.of(2020, 10, 6);
        LocalDate endPeriod = LocalDate.of(2021, 6, 12);
        LocalDate currentDay = startPeriod;
        List<Match> matches = matchRepository.findAll();

        while (currentDay.isBefore(endPeriod)) {
            Map<String, Integer> times = new HashMap<>();
            for (int i = 0; i < 500; i++) {
                int users = usersCount != 0 ? usersCount : 1000;
                long userId = random.nextInt(users);
                if (userId == 0) {
                    userId += 1;
                }
                long finalUserId = userId;
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalStateException("UserID = " + finalUserId));
                int totalTime = 0;
                for (int j = 0; j < random.nextInt(15); j++) {
                    totalTime += random.nextInt(30);
                }

                if (!times.containsKey(user.getCountry())) {
                    times.put(user.getCountry(), totalTime);
                } else {
                    times.put(user.getCountry(), times.get(user.getCountry()) + totalTime);
                }

                boolean isMatchDay = false;
                StringBuilder currentMatches = new StringBuilder();
                LocalDate finalCurrentDay = currentDay;
                if (matches.stream().anyMatch(m -> LocalDate.parse(m.getStartTime()).equals(finalCurrentDay))) {
                    isMatchDay = true;
                    List<Match> matchList = matches.stream()
                            .filter(m -> m.getStartTime().equals(String.valueOf(finalCurrentDay)))
                            .collect(Collectors.toList());
                    for (Match match : matchList) {
                        currentMatches.append(match.getHomeTeam()).append("-").append(match.getAwayTeam()).append(" ").append(match.getResult()).append(";");
                    }
                    if (currentMatches.length() != 0) {
                        currentMatches.replace(currentMatches.length() - 1, currentMatches.length(), "");
                    }
                }

                WatchLog watchLog = WatchLog.builder()
                        .userId(userId)
                        .isMatchDay(isMatchDay)
                        .weekday(currentDay.getDayOfWeek().name())
                        .date(currentDay)
                        .totalTime(totalTime)
                        .matches(currentMatches.toString())
                        .mostViewsCountry(times.entrySet()
                                .stream()
                                .max(Map.Entry.comparingByValue())
                                .get()
                                .getKey())
                        .build();
                if (watchLog.isMatchDay()) {
                    watchLog.setTotalTime((int) (watchLog.getTotalTime() * (1 - random.nextDouble(0.3))));
                }
                kafkaMessageService.sendMessage("watchLogsTopic", watchLog);
            }
            currentDay = currentDay.plusDays(1);
        }
    }

    private List<Match> getMatchDataFromCsv() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        List<Match> matches = CsvParser.parse("champions-league-2020-UTC.csv", Match.class);
        matches.forEach(match -> {
            Calendar calendar = Calendar.getInstance();
            formatter.parse(match.getStartTime());
            TemporalAccessor parsed = formatter.parse(match.getStartTime());
            LocalDateTime startTime = LocalDateTime.from(parsed);
            calendar.setTime((Date.from(startTime.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant())));
            match.setStartTime(String.valueOf(startTime.toLocalDate()));
            match.setWeekday(WeekDayMapper.fromIntToStringWeekDay(calendar.get(Calendar.DAY_OF_WEEK)));
        });
        return matches;
    }
}
