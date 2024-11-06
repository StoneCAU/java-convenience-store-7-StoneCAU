package store.controller;

import java.util.List;
import store.domain.product.Product;
import store.domain.product.ProductFactory;
import store.view.OutputView;

public class StoreController {
    private final ProductFactory productFactory;

    public StoreController(ProductFactory productFactory) {
        this.productFactory = productFactory;
    }

    public void run() {
        OutputView.printWelcomeMessage();

        List<Product> products = productFactory.initProducts();
        loadInventory(products);
    }

    private void loadInventory(List<Product> products) {
        OutputView.printInventory(products);
    }


}
