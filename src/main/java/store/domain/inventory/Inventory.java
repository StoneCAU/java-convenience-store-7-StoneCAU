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

    public List<Product> findProductByName(String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name))
                .toList();
    }

    public boolean isInvalidItem(String name) {
        return products.stream().noneMatch(product -> product.getName().equals(name));
    }

    public boolean isExceedQuantity(String name, int quantity) {
        return findQuantityByName(name) < quantity;
    }

    private int findQuantityByName(String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name))
                .mapToInt(Product::getQuantity)
                .sum();
    }
}
