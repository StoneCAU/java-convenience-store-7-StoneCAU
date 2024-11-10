package store.domain.inventory;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.domain.product.Product;
import store.domain.promotion.Promotion;
import store.dto.Order;
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
        if (!isPromotionDay(orderLine.products().getFirst())) return;

        String productName = orderLine.products().getFirst().getName();
        int newQuantity = getPromotionQuantity(orderLine, getFree);
        if (newQuantity == 0) return;

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

    public boolean notPromotionApplicable(OrderLine orderLine) {
        Product product = orderLine.products().getFirst();
        int productStock = product.getQuantity();

        return productStock < orderLine.quantity();
    }

    public boolean promotionApplicable(OrderLine orderLine) {
        return orderLine.products().stream()
                .filter(this::hasPromotion)
                .filter(product -> sufficientQuantityForBenefit(product, orderLine.quantity()))
                .anyMatch(product -> hasNotAdditionProduct(product, orderLine.quantity()));
    }

    public int getNotPromotionQuantity(OrderLine orderLine) {
        return orderLine.products().stream()
                .filter(this::hasPromotion)
                .mapToInt(product -> calculateNotPromotionQuantity(product, orderLine.quantity()))
                .findFirst()
                .orElse(0);
    }

    public void update(Order order, Map<String, Integer> addition) {
        order.orderLines().forEach(orderLine -> {
            deductAdditional(orderLine, addition);
            deductOriginal(orderLine);
        });
    }

    private void deductAdditional(OrderLine orderLine, Map<String, Integer> addition) {
        for (Product product : orderLine.products()) {
            if (!addition.containsKey(product.getName())) return;
            if (product.getPromotion() != null &&
                    quantityIsNotAdded(product, orderLine.quantity(), addition.get(product.getName()))
            ) return;
            if (product.getQuantity() == 0) continue;

            product.setQuantity(product.getQuantity() - 1);

        }
    }

    private void deductOriginal(OrderLine orderLine) {
        int purchaseQuantity = orderLine.quantity();

        for (Product product : orderLine.products()) {
            int deductQuantity = getDeductQuantity(product, purchaseQuantity);
            product.setQuantity(product.getQuantity() - deductQuantity);
            purchaseQuantity -= deductQuantity;

            if (purchaseQuantity <= 0) break;
        }
    }

    private int getDeductQuantity(Product product, int purchaseQuantity) {
        return Math.min(product.getQuantity(), purchaseQuantity);
    }

    private int getPromotionQuantity(OrderLine orderLine, boolean getFree) {
        return orderLine.products().stream()
                .filter(this::hasPromotion)
                .mapToInt(product -> calculatePromotionQuantity(product, orderLine.quantity(), getFree))
                .findFirst()
                .orElse(0);
    }

    private int calculateNotPromotionQuantity(Product product, int purchaseQuantity) {
        Promotion promotion = product.getPromotion();
        int productStock = product.getQuantity();
        int requiredQuantity = promotion.getBuy() + promotion.getGet();
        int discountQuantity = productStock - productStock % requiredQuantity;
        return purchaseQuantity - discountQuantity;
    }

    private int calculatePromotionQuantity(Product product, int purchaseQuantity, boolean getFree) {
        Promotion promotion = product.getPromotion();
        int requiredQuantity = promotion.getBuy() + promotion.getGet();
        int quotient = getQuotient(product.getQuantity(), purchaseQuantity);
        int quantity = quotient / requiredQuantity;
        if (quotient % requiredQuantity >= promotion.getBuy() && getFree) {
            return quantity + 1;
        }
        return quantity;
    }

    private int getQuotient(int productStock, int purchaseQuantity) {
        return Math.min(productStock, purchaseQuantity);
    }

    private boolean quantityIsNotAdded(Product product, int purchaseQuantity, int mapQuantity) {
        Promotion promotion = product.getPromotion();
        int requiredQuantity = promotion.getBuy() + promotion.getGet();
        int quotient = getQuotient(product.getQuantity(), purchaseQuantity);

        return (quotient / requiredQuantity) == mapQuantity;
    }

    private boolean hasPromotion(Product product) {
        return product.getPromotion() != null;
    }

    public boolean isPromotionDay(Product product) {
        if (product.getPromotion() == null) return false;

        LocalDate now = LocalDate.from(DateTimes.now());
        LocalDate start = product.getPromotion().getStart_date();
        LocalDate end = product.getPromotion().getEnd_date();

        return !(now.isBefore(start) || now.isAfter(end));
    }

    private boolean sufficientQuantityForBenefit(Product product, int purchasedQuantity) {
        Promotion promotion = product.getPromotion();

        int promotionStock = product.getQuantity();
        int requiredQuantity = promotion.getBuy() + promotion.getGet();

        return promotionStock - promotionStock % requiredQuantity > purchasedQuantity;
    }

    private boolean hasNotAdditionProduct (Product product, int purchaseQuantity) {
        Promotion promotion = product.getPromotion();
        int buy = promotion.getBuy();
        int requiredQuantity = promotion.getBuy() + promotion.getGet();
        int discountQuantity = purchaseQuantity - requiredQuantity;

        return purchaseQuantity == buy || (discountQuantity % requiredQuantity == 0 && purchaseQuantity > requiredQuantity);
    }

    private int findQuantityByName(String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name))
                .mapToInt(Product::getQuantity)
                .sum();
    }
}
