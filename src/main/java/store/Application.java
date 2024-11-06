package store;

import store.controller.StoreController;
import store.domain.product.ProductFactory;

public class Application {
    public static void main(String[] args) {
        StoreController storeController = new StoreController(new ProductFactory());
        storeController.run();
    }
}
