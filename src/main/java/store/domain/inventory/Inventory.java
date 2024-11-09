package store.domain.inventory;

import java.util.ArrayList;
import java.util.List;
import store.domain.product.Product;

public class Inventory {
    private final List<Product> products;

    public Inventory(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }
}