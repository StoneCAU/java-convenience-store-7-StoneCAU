package store.domain.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.domain.product.Product;
import store.domain.promotion.Promotion;
import store.dto.OrderLine;

public class Inventory {
    private final List<Product> products;

    public Inventory(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public void calculateAddition(Map<String, Integer> addition, OrderLine orderLine, boolean getFree) {
        String productName = orderLine.products().getFirst().getName();
        int newQuantity = getPromotionQuantity(orderLine, getFree);
        addition.put(productName, newQuantity);
    }

    public List<Product> findProductByName(String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name))
                .collect(Collectors.toList());
    }

    public boolean isInvalidItem(String name) {
        return products.stream().noneMatch(product -> product.getName().equals(name));
    }

    public boolean isExceedQuantity(String name, int quantity) {
        return findQuantityByName(name) < quantity;
    }

    private int getPromotionQuantity(OrderLine orderLine, boolean getFree) {
        return orderLine.products().stream()
                .filter(this::hasPromotion)
                .mapToInt(product -> calculatePromotionQuantity(product, orderLine.quantity(), getFree))
                .findFirst()
                .orElse(0);
    }

    private int calculatePromotionQuantity(Product product, int purchaseQuantity, boolean getFree) {
        Promotion promotion = product.getPromotion();
        int requiredQuantity = promotion.getBuy() + promotion.getGet();
        int quantity = purchaseQuantity / requiredQuantity;
        if (purchaseQuantity % requiredQuantity >= promotion.getBuy() && getFree) {
            return quantity + 1;
        }
        return quantity;
    }

    public boolean promotionApplicable(OrderLine orderLine) {
        return orderLine.products().stream()
                .filter(this::hasPromotion)
                .anyMatch(product -> inSufficientQuantityForBenefit(product, orderLine.quantity()));
    }

    private boolean hasPromotion(Product product) {
        return product.getPromotion() != null;
    }

    private boolean inSufficientQuantityForBenefit(Product product, int purchasedQuantity) {
        int promotionStock = product.getQuantity();

        return promotionStock - purchasedQuantity >= 0;
    }


    private int findQuantityByName(String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name))
                .mapToInt(Product::getQuantity)
                .sum();
    }
}
