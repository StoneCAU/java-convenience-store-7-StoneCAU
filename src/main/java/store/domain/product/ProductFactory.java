package store.domain.product;

import java.util.Arrays;
import java.util.List;
import store.domain.promotion.Promotion;
import store.util.FileLoader;

public class ProductFactory {
    private static final String DELIMITER = ",";
    private static final String FILE_NAME = "products.md";

    public List<Product> initProducts(List<Promotion> promotions) {
        return FileLoader.loadMarkdownFile(FILE_NAME)
                .stream()
                .skip(1)
                .map(item -> create(getFields(item), promotions))
                .toList();
    }

    private Product create(List<String> fields, List<Promotion> promotions) {
        String name = fields.getFirst();
        int price = Integer.parseInt(fields.get(1));
        int quantity = Integer.parseInt(fields.get(2));
        String promotionName = parsePromotion(fields.get(3));
        Promotion promotion = findPromotionByName(promotions, promotionName);

        return new Product(name, price, quantity, promotionName, promotion);
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

    private Promotion findPromotionByName(List<Promotion> promotions, String name) {
        return promotions.stream()
                .filter(promotion -> promotion.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}


