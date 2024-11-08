package store.domain.product;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import store.util.FileLoader;

public class ProductFactory {
    private static final String DELIMITER = ",";
    private static final String FILE_NAME = "products.md";

    public Products createProducts() {
        return new Products(processProducts(initProducts()));
    }

    private List<Product> initProducts() {
        return FileLoader.loadMarkdownFile(FILE_NAME)
                .stream()
                .skip(1)
                .map(this::getFields)
                .map(this::create)
                .toList();
    }

    private Product create(List<String> fields) {
        String name = fields.getFirst();
        int price = Integer.parseInt(fields.get(1));
        int quantity = Integer.parseInt(fields.get(2));
        String promotion = parsePromotion(fields.get(3));

        return new Product(name, price, quantity, promotion);
    }

    private List<String> getFields(String item) {
        return Arrays.stream(item.split(DELIMITER))
                .toList();
    }

    private String parsePromotion(String promotion) {
        if (promotion.equals("null")) {
            return "";
        }

        return promotion;
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


