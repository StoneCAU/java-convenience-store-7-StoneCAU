package store;

import store.controller.StoreController;
import store.domain.product.ProductFactory;
import store.domain.promotion.PromotionFactory;
import store.service.InventoryService;
import store.service.OrderService;

public class Application {
    public static void main(String[] args) {
        final ProductFactory productFactory = new ProductFactory();
        final PromotionFactory promotionFactory = new PromotionFactory();
        final InventoryService inventoryService = new InventoryService(productFactory, promotionFactory);
        final OrderService orderService = new OrderService();

        StoreController storeController = new StoreController(inventoryService, orderService);
        storeController.run();
    }
}
