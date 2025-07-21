package academy.teenfuture.crse.repository;

import academy.teenfuture.crse.modal.DemoUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface DemoUserRepository extends JpaRepository<DemoUser, Long> {
    Optional<DemoUser> findByEmail(String email);
}