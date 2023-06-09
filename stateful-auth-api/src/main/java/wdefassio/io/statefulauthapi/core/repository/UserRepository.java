package wdefassio.io.statefulauthapi.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wdefassio.io.statefulauthapi.core.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

}
