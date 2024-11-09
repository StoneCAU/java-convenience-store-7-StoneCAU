package store;

import store.controller.StoreController;
import store.domain.product.ProductFactory;
import store.domain.promotion.PromotionFactory;
import store.service.InventoryService;
import store.service.OrderService;

public class Application {
    public static void main(String[] args) {
        final ProductFactory productFactory = new ProductFactory();
        final InventoryService inventoryService = new InventoryService(productFactory);
        final OrderService orderService = new OrderService();
        final PromotionFactory promotionFactory = new PromotionFactory();

        StoreController storeController = new StoreController(inventoryService, orderService, promotionFactory);
        storeController.run();
    }
}
