package academy.teenfuture.crse.modal;

import academy.teenfuture.crse.Enum.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(nullable = false)
  String firstName = "";

  @Column(nullable = false)
  String lastName = "";

  @Column(nullable = false)
  String email = "";

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  UserRole role = UserRole.Requester;

  @Column(nullable = false)
  String picture = "";

  @Column(nullable = false)
  boolean isFirstLogin = true;

  @Column(nullable = false)
  boolean active = true;
}
