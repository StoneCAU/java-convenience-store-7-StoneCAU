package store.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import store.domain.inventory.Inventory;
import store.domain.product.Product;
import store.domain.product.ProductFactory;
import store.domain.promotion.Promotion;
import store.domain.promotion.PromotionFactory;
import store.dto.Order;
import store.dto.OrderLine;
import store.service.InventoryService;
import store.service.OrderService;

public class InventoryTest {

    @Nested
    @DisplayName("재고 생성 테스트")
    class CreateInventory {

        @Test
        void 일반_재고에_대한_객체가_없으면_생성되어야_한다() {
            // given
            final ProductFactory productFactory = new ProductFactory();
            final PromotionFactory promotionFactory = new PromotionFactory();
            final InventoryService inventoryService = new InventoryService(productFactory, promotionFactory);

            List<Promotion> promotions = promotionFactory.initPromotions();

            // when
            // 콜라,1000,10,탄산2+1
            // 콜라,1000,10,null
            // 오렌지주스,1800,9,MD추천상품
            Inventory inventory = inventoryService.getInventory();

            List<Product> actual = inventory.getProducts();
            List<Product> expected = List.of(
                    new Product("콜라", 1000, 10, "탄산2+1", promotions.getFirst()),
                    new Product("콜라", 1000, 10, "", null),
                    new Product("오렌지주스", 1800, 9, "MD추천상품", promotions.get(1)),
                    new Product("오렌지주스", 1800, 0, "", null)
            );

            // then
            assertThat(actual)
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactlyInAnyOrderElementsOf(expected);
        }
    }

    @Nested
    @DisplayName("재고 기능 테스트")
    class InventoryFunctionTest {

        final ProductFactory productFactory = new ProductFactory();
        final PromotionFactory promotionFactory = new PromotionFactory();
        final InventoryService inventoryService = new InventoryService(productFactory, promotionFactory);
        final OrderService orderService = new OrderService();

        Inventory inventory = inventoryService.getInventory();
        List<String> items = List.of("오렌지주스-1", "콜라-5");
        Order order = orderService.create(items, inventory);

        @Test
        void 프로모션_기간이_아니면_증정_상품을_계산하지_않는다() {
            // given
            final Map<String, Integer> addition = new HashMap<>();
            OrderLine orderLine = order.orderLines().getFirst();
            boolean getFree = true;

            // when
            inventory.calculateAddition(addition, orderLine, getFree);

            // then
            assertThat(addition.size()).isEqualTo(0);
        }

        @Test
        void 프로모션_상품을_덜_가져왔을_때_추가() {
            // given
            final Map<String, Integer> addition = new HashMap<>();
            OrderLine orderLine = order.orderLines().get(1);
            boolean getFree = true;

            // when
            inventory.calculateAddition(addition, orderLine, getFree);

            // then
            assertThat(addition.get("콜라")).isEqualTo(2);
        }

        @Test
        void 프로모션_상품을_덜_가져왔을_때_추가하지_않음() {
            // given
            final Map<String, Integer> addition = new HashMap<>();
            OrderLine orderLine = order.orderLines().get(1);
            boolean getFree = false;

            // when
            inventory.calculateAddition(addition, orderLine, getFree);

            // then
            assertThat(addition.get("콜라")).isEqualTo(1);
        }

        @Test
        void 프로모션_상품이_적용되지_않은_상품의_갯수_확인() {
            // given
            Inventory inventory = inventoryService.getInventory();
            List<String> items = List.of("오렌지주스-1", "콜라-14");
            Order order = orderService.create(items, inventory);

            OrderLine orderLine = order.orderLines().get(1);

            // when
            int notPromotionQuantity = inventory.getNotPromotionQuantity(orderLine);

            assertThat(notPromotionQuantity).isEqualTo(5);
        }
    }
}
