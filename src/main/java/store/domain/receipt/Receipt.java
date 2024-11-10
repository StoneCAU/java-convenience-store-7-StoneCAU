package store.domain.receipt;

import java.util.Map;
import store.dto.Order;

public class Receipt {
    private Order order;
    private Map<String, Integer> addition;
    private boolean membershipDiscount;

    private Receipt(Order order, Map<String, Integer> addition, boolean membershipDiscount) {
        this.order = order;
        this.addition = addition;
        this.membershipDiscount = membershipDiscount;
    }

    public static Receipt generate(Order order, Map<String, Integer> addition, boolean membershipDiscount) {
        return new Receipt(order, addition, membershipDiscount);
    }
}
