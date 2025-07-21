package academy.teenfuture.crse.modal;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "demo-user")
@Data
public class DemoUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String email = "";
    String password = "";
    String phoneNumber = "";
}
