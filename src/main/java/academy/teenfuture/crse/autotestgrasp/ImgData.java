package academy.teenfuture.crse.autotestgrasp;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "imgByte")
@Data
public class ImgData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

  @Column
  @OrderBy("dateTime DESC")
  LocalDateTime dateTime;

    @Lob
  @Column(length = 100000)
    byte[] img;

  public ImgData() {
    this.dateTime = LocalDateTime.now();
  }
}