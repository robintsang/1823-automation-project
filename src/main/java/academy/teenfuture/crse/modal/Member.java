package academy.teenfuture.crse.modal; 

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Member")
@Data
public class Member {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;
  @Column(nullable= false)
  int point;
}