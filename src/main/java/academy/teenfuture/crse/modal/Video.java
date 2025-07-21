package academy.teenfuture.crse.modal;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "video")
@Data
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Lob
    byte[] video;

    public byte[] getData() {
      return video;
  }

  public void setData(byte[] video) {
    this.video = video;
}
}