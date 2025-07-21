package academy.teenfuture.crse.repository;

import academy.teenfuture.crse.modal.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
  Member findById(int id);
}