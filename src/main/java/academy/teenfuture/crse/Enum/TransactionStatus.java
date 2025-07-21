package academy.teenfuture.crse.Enum;

import java.util.List;
import java.util.Random;

public enum TransactionStatus {
    NewRequest,
    PendingApproval,
    Approved,
    Picking,

    Delivered,
    Completed,

    Cancel,
    Rejected,
    Closed;

    private static final List<TransactionStatus> VALUES =
            List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public static TransactionStatus random() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

/*
    PendingReturn,
    PendingNoReturn,
    PendingSalesReturn,*/
    }
