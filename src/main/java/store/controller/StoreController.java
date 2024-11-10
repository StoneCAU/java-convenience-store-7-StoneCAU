package store.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import store.domain.inventory.Inventory;
import store.domain.product.Product;
import store.domain.receipt.Receipt;
import store.dto.Order;
import store.dto.OrderLine;
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

        do {
            loadInventory(inventory);
            Order order = getOrder(inventory);
            Receipt receipt = getReceipt(order, inventory);
            OutputView.printReceipt(receipt);
        } while (additionalPurchase());
    }

    private boolean additionalPurchase() {
        return retryOnError(() -> Parser.parseResponse(InputView.inputAdditionalPurchase()));
    }

    private Receipt getReceipt(Order order, Inventory inventory) {
        order = getNewOrder(order, inventory);
        Map<String, Integer> addition = getAddition(order, inventory);
        boolean isMembershipDiscount = getIsMembershipDiscount();

        inventory.update(order, addition);

        return Receipt.generate(order, addition, isMembershipDiscount);
    }

    private boolean getIsMembershipDiscount() {
        return retryOnError(() -> Parser.parseResponse(InputView.inputMembershipDiscount()));
    }

    private Order getNewOrder(Order order, Inventory inventory) {
        List<OrderLine> newOrderLines = order.orderLines()
                .stream()
                .map(orderLine -> {
                    boolean getFree = getFreeProduct(inventory, orderLine);
                    boolean purchase = getNotDiscount(inventory, orderLine);
                    return orderService.getNewOrderLine(inventory, orderLine, getFree, purchase);
                })
                .toList();

        return new Order(newOrderLines);
    }

    private Map<String, Integer> getAddition(Order order, Inventory inventory) {
        Map<String, Integer> addition = new HashMap<>();

        order.orderLines()
                .forEach(orderLine -> {
                    boolean getFree = getFreeProduct(inventory, orderLine);
                    inventory.calculateAddition(addition, orderLine, getFree);
                });

        return addition;
    }

    private boolean getNotDiscount(Inventory inventory, OrderLine orderLine) {
        if (inventory.notPromotionApplicable(orderLine)) {
            Product product = orderLine.products().getFirst();
            int notPromotionQuantity = inventory.getNotPromotionQuantity(orderLine);
            return retryOnError(() -> Parser.parseResponse(InputView.getNotDiscount(product, notPromotionQuantity)));
        }
        return true;
    }

    private boolean getFreeProduct(Inventory inventory, OrderLine orderLine) {
        if (inventory.promotionApplicable(orderLine)) {
            Product product = orderLine.products().getFirst();
            return retryOnError(() -> Parser.parseResponse(InputView.getFreeProduct(product)));
        }
        return false;
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
