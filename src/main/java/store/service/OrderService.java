package store.service;

import java.util.List;
import store.dto.Order;
import store.dto.OrderLine;

public class OrderService {
    private static final String DELIMITER = "-";

    public Order create(List<String> items) {
        return makeAnOrder(items);
    }

    private Order makeAnOrder(List<String> items) {
        return new Order(items.stream()
                .map(this::makeAnOrderLine)
                .toList());
    }

    private OrderLine makeAnOrderLine(String item) {
        String[] split = item.split(DELIMITER);

        String name = split[0];
        int quantity = Integer.parseInt(split[1]);

        return new OrderLine(name, quantity);
    }
}
