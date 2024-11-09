package store.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import store.domain.inventory.Inventory;
import store.domain.product.Product;
import store.domain.product.ProductFactory;
import store.domain.promotion.Promotion;
import store.domain.promotion.PromotionFactory;

public class InventoryService {
    private final ProductFactory productFactory;
    private final PromotionFactory promotionFactory;

    public InventoryService(ProductFactory productFactory, PromotionFactory promotionFactory) {
        this.productFactory = productFactory;
        this.promotionFactory = promotionFactory;
    }

    public Inventory getInventory() {
        List<Promotion> promotions = promotionFactory.initPromotions();
        List<Product> products = productFactory.initProducts(promotions);
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
        return new Product(product.getName(), product.getPrice(), 0, "", null);
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
                && product.getPromotionName() != null
                && !product.getPromotionName().isEmpty();
    }
}
