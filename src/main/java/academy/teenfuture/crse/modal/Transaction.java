package academy.teenfuture.crse.modal;

import academy.teenfuture.crse.Enum.TransactionStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    TransactionStatus status = TransactionStatus.NewRequest;

    @Column(nullable = false, length = 50)
    String itemCode = "";

    @Column(nullable = false)
    int deliveredQty = 0;

    @Column(nullable = false)
    int userRequestQty = 0;

    @Column(nullable = false)
    int userNoReturnQty = 0;

    @Column(nullable = false)
    int userReturnedQty = 0;

    @Column(nullable = false)
    int userSalesReturnedQty = 0;

    @Column(nullable = false)
    int userReturnRequestQty = 0;

    @Column(nullable = false)
    int userSalesReturnRequestQty = 0;

    @Column(nullable = false, length = 50)
    String warehouse = "";

    @Column(nullable = false)
    int warehouseQty = 0;

    @Column(nullable = false, length = 50)
    String location = "";

    @Column(nullable = false, length = 50)
    String projectCode = "";

    @Column(nullable = false, length = 50)
    String orderId = "";

    @Column(nullable = false, length = 50)
    String sku = "";

    @Column(nullable = false, length = 50)
    String description = "";

    @Column(nullable = false, length = 50)
    boolean newRequest = true;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    Date createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    Date lastModifiedAt;

    public int getBalanceQty() {
        return deliveredQty - userRequestQty - userNoReturnQty;
    }
}
