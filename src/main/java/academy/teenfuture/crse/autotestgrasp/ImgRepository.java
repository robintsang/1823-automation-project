package academy.teenfuture.crse.autotestgrasp;

import academy.teenfuture.crse.autotestgrasp.ImgData;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

import java.util.Optional;

public interface ImgRepository extends JpaRepository<ImgData, Long> {
  Optional<ImgData> findByImg(byte[] img);

  Optional<ImgData> findFirstByOrderByDateTimeDesc();
}