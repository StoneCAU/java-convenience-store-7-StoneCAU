package store.controller;

import java.util.List;
import java.util.function.Supplier;
import store.domain.inventory.Inventory;
import store.dto.Order;
import store.exception.StoreException;
import store.service.InventoryService;
import store.service.OrderService;
import store.util.Parser;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final InventoryService inventoryService;
    private final OrderService orderService;

    public StoreController(InventoryService inventoryService, OrderService orderService) {
        this.inventoryService = inventoryService;
        this.orderService = orderService;
    }

    public void run() {
        OutputView.printWelcomeMessage();

        Inventory inventory = inventoryService.getInventory();
        loadInventory(inventory);

        Order order = getOrder(inventory);
    }

    private void loadInventory(Inventory inventory) {
        OutputView.printInventory(inventory);
    }

    private Order getOrder(Inventory inventory) {
        return retryOnError(() -> {
            String input = InputView.purchaseProducts();
            List<String> items = Parser.parseItems(input);
            return orderService.create(items, inventory);
        });
    }

    private <T> T retryOnError(Supplier<T> supplier) {
        while(true) {
            try {
                return supplier.get();
            } catch (StoreException e) {
                OutputView.printErrorMessage(e.getMessage());
            }
        }
    }

}
