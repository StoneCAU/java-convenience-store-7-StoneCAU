package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import store.domain.promotion.Promotion;
import store.domain.promotion.PromotionFactory;

public class PromotionTest {

    @Test
    void 프로모션_객체_생성_테스트() {
        // given
        final PromotionFactory promotionFactory = new PromotionFactory();

        // when
        // 탄산2+1,2,1,2024-01-01,2024-12-31
        // MD추천상품,1,1,2025-01-01,2025-12-31
        List<Promotion> actual = promotionFactory.initPromotions();
        List<Promotion> expected = List.of(
                new Promotion("탄산2+1", 2, 1,
                LocalDate.of(2024,1,1), LocalDate.of(2024,12,31)),

                new Promotion("MD추천상품", 1, 1,
                        LocalDate.of(2025,1,1), LocalDate.of(2025,12,31))
        );

        // then
        assertThat(actual)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expected);
    }
}
