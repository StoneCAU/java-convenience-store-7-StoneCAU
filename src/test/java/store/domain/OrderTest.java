package store.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import store.domain.inventory.Inventory;
import store.domain.product.Product;
import store.domain.product.ProductFactory;
import store.domain.promotion.PromotionFactory;
import store.dto.Order;
import store.dto.OrderLine;
import store.exception.StoreException;
import store.service.InventoryService;
import store.service.OrderService;

public class OrderTest {

    final ProductFactory productFactory = new ProductFactory();
    final PromotionFactory promotionFactory = new PromotionFactory();
    final InventoryService inventoryService = new InventoryService(productFactory, promotionFactory);
    final OrderService orderService = new OrderService();


    @Nested
    @DisplayName("객체 생성 테스트")
    class CreateOrder {
        @Test
        void 주문_객체_생성_테스트() {
            // given
            Inventory inventory = inventoryService.getInventory();
            List<String> items = List.of("오렌지주스-1", "콜라-3");

            // when
            Order order = orderService.create(items, inventory);
            OrderLine firstOrderLine = order.orderLines().getFirst();
            OrderLine secondOrderLine = order.orderLines().get(1);

            Product firstProduct = firstOrderLine.products().getFirst();
            Product secondProduct = secondOrderLine.products().getFirst();

            // then
            assertAll(
                    () -> assertThat(firstProduct.getName()).isEqualTo("오렌지주스"),
                    () -> assertThat(firstOrderLine.quantity()).isEqualTo(1),
                    () -> assertThat(secondProduct.getName()).isEqualTo("콜라"),
                    () -> assertThat(secondOrderLine.quantity()).isEqualTo(3)
            );
        }
    }

    @Nested
    @DisplayName("예외 처리 테스트")
    class OrderExceptionTest {
        @Test
        void 존재하지_않는_상품명을_입력_시_예외를_발생시킨다() {
            // given
            Inventory inventory = inventoryService.getInventory();
            List<String> items = List.of("너무-1", "어려워-8");

            // when & then
            assertThatThrownBy(() -> orderService.create(items, inventory))
                    .isInstanceOf(StoreException.class)
                    .hasMessage("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
        }

        @Test
        void 재고_수량보다_많은_수량을_입력_시_예외를_발생시킨다() {
            // given
            Inventory inventory = inventoryService.getInventory();
            List<String> items = List.of("오렌지주스-100", "콜라-800");

            // when & then
            assertThatThrownBy(() -> orderService.create(items, inventory))
                    .isInstanceOf(StoreException.class)
                    .hasMessage("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }

        @Test
        void 주문_수량이_0일_시_예외를_발생시킨다() {
            // given
            Inventory inventory = inventoryService.getInventory();
            List<String> items = List.of("콜라-0");

            // when & then
            assertThatThrownBy(() -> orderService.create(items, inventory))
                    .isInstanceOf(StoreException.class)
                    .hasMessage("[ERROR] 0 이상의 수량을 입력해주세요.");
        }
    }
}
