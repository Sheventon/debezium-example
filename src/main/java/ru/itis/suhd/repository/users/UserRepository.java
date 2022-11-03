package ru.itis.suhd.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.suhd.model.users.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
