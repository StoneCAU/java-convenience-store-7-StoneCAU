package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.product.Product;
import store.domain.product.ProductFactory;
import store.domain.promotion.Promotion;
import store.domain.promotion.PromotionFactory;

public class ProductTest {

    @Test
    void 상품_객체_생성_테스트() {
        // given
        final PromotionFactory promotionFactory = new PromotionFactory();
        final ProductFactory productFactory = new ProductFactory();

        List<Promotion> promotions = promotionFactory.initPromotions();

        // when
        List<Product> actual = productFactory.initProducts(promotions);
        List<Product> expected = List.of(
                new Product("콜라", 1000, 10, "탄산2+1", promotions.getFirst()),
                new Product("콜라", 1000, 10, "", null),
                new Product("오렌지주스", 1800, 9, "MD추천상품", promotions.get(1))
                );

        // then
        assertThat(actual)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expected);
    }
}
