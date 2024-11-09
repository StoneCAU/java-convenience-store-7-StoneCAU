package store.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import store.domain.inventory.Inventory;
import store.domain.product.Product;
import store.domain.product.ProductFactory;

public class InventoryService {
    private final ProductFactory productFactory;

    public InventoryService(ProductFactory productFactory) {
        this.productFactory = productFactory;
    }

    public Inventory getInventory() {
        List<Product> products = productFactory.initProducts();
        return new Inventory(processProducts(products));
    }

    private Map<String, Integer> countProductStock(List<Product> products) {
        return products.stream()
                .collect(Collectors.groupingBy(Product::getName, Collectors.summingInt(p -> 1)));
    }

    private List<Product> processProducts(List<Product> products) {
        Map<String, Integer> stockMap = countProductStock(products);

        return products.stream()
                .flatMap(product -> processProduct(product, stockMap))
                .collect(Collectors.toList());
    }

    private Product createNormalProduct(Product product) {
        return new Product(product.getName(), product.getPrice(), 0, "");
    }

    private Stream<Product> processProduct(Product product, Map<String, Integer> stockMap) {
        if (normalProductQuantityIsZero(product, stockMap)) {
            return Stream.of(product, createNormalProduct(product));
        } else {
            return Stream.of(product);
        }
    }

    private boolean normalProductQuantityIsZero(Product product, Map<String, Integer> stockMap) {
        return stockMap.get(product.getName()) == 1
                && product.getPromotion() != null
                && !product.getPromotion().isEmpty();
    }
}
