package store.domain.product;

import java.util.Arrays;
import java.util.List;
import store.util.FileLoader;

public class ProductFactory {
    private static final String DELIMITER = ",";

    public List<Product> initProducts() {
        return FileLoader.loadMarkdownFile("products.md")
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
}
