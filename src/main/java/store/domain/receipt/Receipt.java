package store.domain.receipt;

import java.util.Map;
import store.domain.product.Product;
import store.dto.Order;
import store.dto.OrderLine;

public class Receipt {
    private final static double DISCOUNT = 0.3;
    private final static int MAX_DISCOUNT_PRICE = 8000;

    private final Order order;
    private final Map<String, Integer> addition;
    private final boolean isMembershipDiscount;

    private final int totalPrice;
    private final int totalQuantity;
    private final int promotionDiscount;
    private final int membershipDiscount;

    private Receipt(Order order, Map<String, Integer> addition, boolean isMembershipDiscount) {
        this.order = order;
        this.addition = addition;
        this.isMembershipDiscount = isMembershipDiscount;
        this.totalPrice = calculateTotalPrice();
        this.totalQuantity = calculateTotalQuantity();
        this.promotionDiscount = calculatePromotionDiscount();
        this.membershipDiscount = calculateMembershipDiscount();
    }

    public static Receipt generate(Order order, Map<String, Integer> addition, boolean membershipDiscount) {
        return new Receipt(order, addition, membershipDiscount);
    }

    public Order getOrder() {
        return order;
    }

    public Map<String, Integer> getAddition() {
        return addition;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getFinalPayment() {
        return totalPrice - (promotionDiscount + membershipDiscount);
    }

    private int calculateMembershipDiscount() {
        int totalDiscount = 0;

        if (isMembershipDiscount) {
            int afterPromotion = totalPrice - promotionDiscount;
            totalDiscount = (int) (afterPromotion * DISCOUNT);

            if (totalDiscount > MAX_DISCOUNT_PRICE) {
                totalDiscount = MAX_DISCOUNT_PRICE;
            }
        }

        return totalDiscount;
    }

    private int calculatePromotionDiscount() {
        return addition.entrySet().stream()
                .mapToInt(entry -> {
                    String name = entry.getKey();
                    int quantity = entry.getValue();
                    int price = findPrice(order, name);

                    return price * quantity;
                }).sum();
    }

    private int calculateTotalQuantity() {
        return order.orderLines().stream()
                .mapToInt(OrderLine::quantity).sum();
    }

    private int calculateTotalPrice() {
        return order.orderLines().stream()
                .mapToInt(orderLine -> {
                    Product product = orderLine.products().getFirst();
                    return orderLine.quantity() * product.getPrice();
                }).sum();
    }

    private int findPrice(Order order, String name) {
        return this.order.orderLines().stream()
                .map(orderLine -> orderLine.products().getFirst())
                .filter(product -> product.getName().equals(name))
                .mapToInt(Product::getPrice)
                .findFirst()
                .orElse(0);
    }
}
