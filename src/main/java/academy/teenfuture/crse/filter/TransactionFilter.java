package academy.teenfuture.crse.filter;

import academy.teenfuture.crse.Enum.TransactionStatus;
import lombok.Data;

import java.util.Date;

@Data
public class TransactionFilter {

    TransactionStatus status;
    String itemCode;
    int userRequestQty;
    int deliveredToUserQty;
    int userReturnRequestQty;
    int userReturnedQty;
    int userKeepRequestQty;
    int userKeepingQty;
    int userReturnKeepingRequestQty;
    int userReturnKeepingQty;
    String warehouse;
    int warehouseQty;
    String location;
    String projectCode;
    String orderId;
    String sku;
    Date createdAt;
    Date lastModifiedAt;
}
