package store.view;

import java.util.Map;
import store.domain.inventory.Inventory;
import store.domain.product.Product;
import store.domain.receipt.Receipt;
import store.dto.Order;

public class OutputView {
    private static final String NEW_LINE = System.lineSeparator();
    private static final String GREETING_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String INVENTORY_INFORMATION_MESSAGE = "현재 보유하고 있는 상품입니다.";
    private static final String RECEIPT_TITLE = "==============W 편의점================";
    private static final String RECEIPT_COLUMN_NAMES = "%-12s\t\t\t%s\t\t%s%n";
    private static final String RECEIPT_THREE_COLUMN = "%-12s\t\t\t%d\t\t%s%n";
    private static final String RECEIPT_ADDITION_TITLE = "==============증\t정================";
    private static final String RECEIPT_ADDITION = "%-12s\t\t\t%d%n";
    private static final String RECEIPT_SEPARATOR = "=====================================";
    private static final String RECEIPT_DISCOUNT = "%-12s\t\t\t\t\t%s%n";


    public static void printWelcomeMessage() {
        System.out.println(GREETING_MESSAGE);
        System.out.println(INVENTORY_INFORMATION_MESSAGE);
        System.out.print(NEW_LINE);
    }

    public static void printInventory(Inventory inventory) {
        inventory.getProducts().forEach(System.out::println);
        System.out.print(NEW_LINE);
    }

    public static void printErrorMessage(String message) {
        System.out.println(message);
        System.out.print(NEW_LINE);
    }

    public static void printReceipt(Receipt receipt) {
        System.out.print(NEW_LINE);
        printPurchaseProducts(receipt.getOrder());
        printAdditionProducts(receipt.getAddition());
        printPayment(receipt);
    }

    private static void printPurchaseProducts(Order order) {
        System.out.println(RECEIPT_TITLE);
        System.out.printf((RECEIPT_COLUMN_NAMES), "상품명", "수량", "금액");

        order.orderLines()
                .forEach(orderLine -> {
                    Product product = orderLine.products().getFirst();
                    int price = orderLine.quantity() * product.getPrice();
                    System.out.printf((RECEIPT_THREE_COLUMN), product.getName(), orderLine.quantity(), formatPrice(price));
                });
    }

    private static void printAdditionProducts(Map<String, Integer> addition) {
        System.out.println(RECEIPT_ADDITION_TITLE);

        addition.forEach((key, value) -> System.out.printf((RECEIPT_ADDITION), key, value));
    }

    private static void printPayment(Receipt receipt) {
        System.out.println(RECEIPT_SEPARATOR);
        System.out.printf((RECEIPT_THREE_COLUMN), "총구매액", receipt.getTotalQuantity(), formatPrice(receipt.getTotalPrice()));
        System.out.printf((RECEIPT_DISCOUNT), "행사할인", formatDiscount(receipt.getPromotionDiscount()));
        System.out.printf((RECEIPT_DISCOUNT), "멤버십할인", formatDiscount(receipt.getMembershipDiscount()));
        System.out.printf((RECEIPT_DISCOUNT), "내실돈", formatPrice(receipt.getFinalPayment()));
    }

    private static String formatPrice(int price) {
        return String.format("%,d", price);
    }

    private static String formatDiscount(int discount) {
        return "-" + formatPrice(discount);
    }
}
