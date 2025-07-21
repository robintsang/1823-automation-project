package academy.teenfuture.crse.modal;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "remark")
@Data
public class Remark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    Transaction transaction;

    @Column(nullable = false)
    Long userId;

    @Column(nullable = false)
    String description;

    @Column(nullable = false)
    @CreationTimestamp
    Date createdAt;
}
