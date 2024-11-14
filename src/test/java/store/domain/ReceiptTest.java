package store.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import store.domain.inventory.Inventory;
import store.domain.product.Product;
import store.domain.product.ProductFactory;
import store.domain.promotion.PromotionFactory;
import store.domain.receipt.Receipt;
import store.dto.Order;
import store.dto.OrderLine;
import store.service.InventoryService;
import store.service.OrderService;

public class ReceiptTest {

    final ProductFactory productFactory = new ProductFactory();
    final PromotionFactory promotionFactory = new PromotionFactory();
    final InventoryService inventoryService = new InventoryService(productFactory, promotionFactory);
    final OrderService orderService = new OrderService();

    Inventory inventory = inventoryService.getInventory();
    List<String> items = List.of("오렌지주스-1", "콜라-5");
    Order order = orderService.create(items, inventory);
    boolean isMembershipDiscount = true;
    Map<String, Integer> addition = new HashMap<>();

    @Nested
    @DisplayName("객체 생성 테스트")
    class CreateReceiptTest {

        @BeforeEach
        void setUp() {
            inventory.calculateAddition(addition, order.orderLines().getLast(), true);
        }

        @Test
        void 객체_생성_테스트() {
            // given
            Receipt receipt = Receipt.generate(order, addition, isMembershipDiscount);

            // when & then
            assertAll(
                    () -> assertThat(receipt.getOrder()).isEqualTo(order),
                    () -> assertThat(receipt.getAddition()).isEqualTo(addition)
            );
        }

        @Test
        void 총_구매액_계산() {
            // given
            Receipt receipt = Receipt.generate(order, addition, isMembershipDiscount);

            // when
            int actual = receipt.getTotalPrice();
            int excepted = order.orderLines().stream()
                    .mapToInt(orderLine -> {
                        Product product = orderLine.products().getFirst();
                        return orderLine.quantity() * product.getPrice();
                    }).sum();

            // then
            assertThat(actual).isEqualTo(excepted);
        }

        @Test
        void 총_수량_계산() {
            // given
            Receipt receipt = Receipt.generate(order, addition, isMembershipDiscount);

            // when
            int actual = receipt.getTotalQuantity();
            int excepted = order.orderLines().stream()
                    .mapToInt(OrderLine::quantity).sum();

            // then
            assertThat(actual).isEqualTo(excepted);
        }

        private int findPrice(Order order, String name) {
            return order.orderLines().stream()
                    .map(orderLine -> orderLine.products().getFirst())
                    .filter(product -> product.getName().equals(name))
                    .mapToInt(Product::getPrice)
                    .findFirst()
                    .orElse(0);
        }

        @Test
        void 행사_할인_계산() {
            // given
            Receipt receipt = Receipt.generate(order, addition, isMembershipDiscount);

            // when
            int actual = receipt.getPromotionDiscount();
            int excepted = addition.entrySet().stream()
                    .mapToInt(entry -> {
                        String name = entry.getKey();
                        int quantity = entry.getValue();
                        int price = findPrice(order, name);

                        return price * quantity;
                    }).sum();

            // then
            assertThat(actual).isEqualTo(excepted);
        }

        @Test
        void 멤버십_할인_계산() {
            // given
            Receipt receipt = Receipt.generate(order, addition, isMembershipDiscount);

            // when
            int actual = receipt.getMembershipDiscount();
            int excepted = (int) ((receipt.getTotalPrice() - receipt.getPromotionDiscount()) * 0.3);

            // then
            assertThat(actual).isEqualTo(excepted);
        }

        @Test
        void 지불_금액_계산() {
            // given
            Receipt receipt = Receipt.generate(order, addition, isMembershipDiscount);

            // when
            int actual = receipt.getFinalPayment();
            int excepted = receipt.getTotalPrice() - receipt.getMembershipDiscount() - receipt.getPromotionDiscount();

            // then
            assertThat(actual).isEqualTo(excepted);
        }
    }

}
