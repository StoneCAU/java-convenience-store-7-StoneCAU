package store.service;

import java.util.List;
import store.domain.inventory.Inventory;
import store.dto.Order;
import store.dto.OrderLine;
import store.exception.ErrorMessage;
import store.exception.StoreException;

public class OrderService {
    private static final String DELIMITER = "-";

    public Order create(List<String> items, Inventory inventory) {
        return makeAnOrder(items, inventory);
    }

    private Order makeAnOrder(List<String> items, Inventory inventory) {
        return new Order(items.stream()
                .map(item -> makeAnOrderLine(item, inventory))
                .toList());
    }

    private OrderLine makeAnOrderLine(String item, Inventory inventory) {
        String[] split = item.split(DELIMITER);

        String name = split[0];
        validateName(name, inventory);

        int quantity = Integer.parseInt(split[1]);
        validateQuantity(name, quantity, inventory);

        return new OrderLine(name, quantity);
    }

    private void validateName(String name, Inventory inventory) {
        if (inventory.isInvalidItem(name)) {
            throw new StoreException(ErrorMessage.INVALID_ITEM.getMessage());
        }
    }

    private void validateQuantity(String name, int quantity, Inventory inventory) {
        if (inventory.isExceedQuantity(name, quantity)) {
            throw new StoreException(ErrorMessage.EXCEED_QUANTITY.getMessage());
        }
    }
}
