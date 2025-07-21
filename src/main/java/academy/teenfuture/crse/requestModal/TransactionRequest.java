package academy.teenfuture.crse.requestModal;

import lombok.Data;

@Data
public class TransactionRequest {
    Long userId;
    String location;
    String itemCode;
    String description;
    String warehouse;
    int userRequestQty;
    String projectCode;
    String orderId;
    String sku;
    String remark;
}
