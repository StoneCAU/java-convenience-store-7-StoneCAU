package store.controller;

import store.domain.product.ProductFactory;
import store.domain.product.Products;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final ProductFactory productFactory;

    public StoreController(ProductFactory productFactory) {
        this.productFactory = productFactory;
    }

    public void run() {
        OutputView.printWelcomeMessage();

        Products products = productFactory.createProducts();
        loadInventory(products);

        InputView.purchaseProducts();
    }

    private void loadInventory(Products products) {
        OutputView.printInventory(products);
    }


}
