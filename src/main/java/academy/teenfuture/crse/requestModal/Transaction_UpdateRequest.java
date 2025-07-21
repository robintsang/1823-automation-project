package academy.teenfuture.crse.requestModal;

import academy.teenfuture.crse.Enum.TransactionStatus;
import lombok.Data;

@Data
public class Transaction_UpdateRequest {
    Long userId;
    String remark = "";
    TransactionStatus status;
    int userRequestQty;
    int deliveredToUserQty;
    int userReturnRequestQty;
    int userReturnedQty;
    int userKeepRequestQty;
    int userKeepingQty;
    int userReturnKeepingRequestQty;
    int userReturnKeepingQty;
}
