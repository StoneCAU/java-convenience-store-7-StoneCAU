package store.controller;

import java.util.List;
import java.util.function.Supplier;
import store.domain.inventory.InventoryManager;
import store.domain.product.ProductFactory;
import store.domain.inventory.Inventory;
import store.dto.Order;
import store.exception.StoreException;
import store.service.InventoryService;
import store.util.Parser;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final InventoryService inventoryService;

    public StoreController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void run() {
        OutputView.printWelcomeMessage();

        Inventory inventory = inventoryService.getInventory();
        loadInventory(inventory);

        InventoryManager inventoryManager = new InventoryManager(inventory);
        Order order = getOrder(inventoryManager);
    }

    private void loadInventory(Inventory inventory) {
        OutputView.printInventory(inventory);
    }

    private Order getOrder(InventoryManager inventoryManager) {
        List<String> items = retryOnError(() -> {
            String input = InputView.purchaseProducts();
            return Parser.parseItems(input);
        });

        return inventoryManager.purchase(items);
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
