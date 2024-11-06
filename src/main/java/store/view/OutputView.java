package store.view;

import java.util.List;
import store.domain.product.Product;

public class OutputView {
    private static final String NEW_LINE = System.lineSeparator();
    private static final String GREETING_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String INVENTORY_INFORMATION_MESSAGE = "현재 보유하고 있는 상품입니다.";

    public static void printWelcomeMessage() {
        System.out.println(GREETING_MESSAGE);
        System.out.println(INVENTORY_INFORMATION_MESSAGE);
        System.out.print(NEW_LINE);
    }

    public static void printInventory(List<Product> products) {
        products.forEach(product -> {
            System.out.println(product.toString());
        });
    }
}
