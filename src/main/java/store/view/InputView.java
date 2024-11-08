package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    private final static String PURCHASE_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";

    public static String purchaseProducts() {
        System.out.println(PURCHASE_MESSAGE);
        return input();
    }

    private static String input() {
        return Console.readLine();
    }
}
