package ru.itis.suhd.repository.stats;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.suhd.model.stats.UserMatchStat;

import java.time.LocalDate;
import java.util.Optional;

public interface UserMatchRepository extends JpaRepository<UserMatchStat, Long> {

    Optional<UserMatchStat> findByDate(LocalDate date);
}
