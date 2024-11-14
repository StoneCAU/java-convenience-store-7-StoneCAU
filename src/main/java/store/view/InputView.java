package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.domain.product.Product;

public class InputView {
    private final static String NEW_LINE = System.lineSeparator();
    private final static String PURCHASE_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private final static String GET_FREE_MESSAGE = "현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
    private final static String NOT_DISCOUNT_MESSAGE = "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    private final static String MEMBERSHIP_DISCOUNT_MESSAGE = "멤버십 할인을 받으시겠습니까? (Y/N)";
    private final static String ADDITIONAL_PURCHASE_MESSAGE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";

    public static String purchaseProducts() {
        System.out.println(PURCHASE_MESSAGE);
        return input();
    }

    public static String getFreeProduct(Product product) {
        System.out.print(NEW_LINE);
        System.out.printf((GET_FREE_MESSAGE) + "%n", product.getName());
        return input();
    }

    public static String getNotDiscount(Product product, int quantity) {
        System.out.print(NEW_LINE);
        System.out.printf((NOT_DISCOUNT_MESSAGE) + "%n", product.getName(), quantity);
        return input();
    }

    public static String inputMembershipDiscount() {
        System.out.print(NEW_LINE);
        System.out.println(MEMBERSHIP_DISCOUNT_MESSAGE);
        return input();
    }

    public static String inputAdditionalPurchase() {
        System.out.printf(NEW_LINE);
        System.out.println(ADDITIONAL_PURCHASE_MESSAGE);
        return input();
    }

    private static String input() {
        return Console.readLine();
    }
}
