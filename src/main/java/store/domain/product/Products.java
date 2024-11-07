package store.domain.product;

import java.util.ArrayList;
import java.util.List;

public class Products {
    private final List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }
}
