package ukathon.mayo.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ukathon.mayo.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByNickname(String nickname);
}
