package store.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import store.exception.StoreException;

public class ParserTest {

    @Nested
    @DisplayName("상품 입력 테스트")
    class InputProductsTest {
        @Test
        void 정상_파싱_테스트() {
            // given
            String input = "[콜라-5],[사이다-3]";

            // when & then
            assertDoesNotThrow(() -> Parser.parseItems(input));
        }

        @Test
        void 입력_사이에_공백이_있으면_예외가_발생한다() {
            // given
            String input = "[콜라-5], [사이다-3]";

            // when & then
            assertThatThrownBy(() -> Parser.parseItems(input))
                    .isInstanceOf(StoreException.class)
                    .hasMessageContaining("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }

        @Test
        void 입력_형식에_어긋나면_예외가_발생한다() {
            // given
            String input = "콜라-5";

            // when & then
            assertThatThrownBy(() -> Parser.parseItems(input))
                    .isInstanceOf(StoreException.class)
                    .hasMessageContaining("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }

        @Test
        void 중복된_상품을_입력하면_예외가_발생한다() {
            // given
            String input = "[콜라-5],[콜라-3]";

            // when & then
            assertThatThrownBy(() -> Parser.parseItems(input))
                    .isInstanceOf(StoreException.class)
                    .hasMessageContaining("[ERROR] 중복된 상품 입력입니다.");
        }
    }

    @Nested
    @DisplayName("Y/N 입력 테스트")
    class YesOrNoInputTest {
        @Test
        void Y를_입력_시에_true_를_반환한다() {
            // given
            String input = "Y";

            // when
            boolean actual = Parser.parseResponse(input);

            // then
            assertThat(actual).isTrue();
        }

        @Test
        void N를_입력_시에_false_를_반환한다() {
            // given
            String input = "N";

            // when
            boolean actual = Parser.parseResponse(input);

            // then
            assertThat(actual).isFalse();
        }

        @Test
        void 다른_문자를_입력_시에_예외가_발생한다() {
            // given
            String input = "abcd";

            // when & then
            assertThatThrownBy(() -> Parser.parseResponse(input))
                    .isInstanceOf(StoreException.class)
                    .hasMessage("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
        }

        @Test
        void 숫자를_입력_시에_예외가_발생한다() {
            // given
            String input = "123";

            // when & then
            assertThatThrownBy(() -> Parser.parseResponse(input))
                    .isInstanceOf(StoreException.class)
                    .hasMessage("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
        }

        @Test
        void 공백을_입력_시에_예외가_발생한다() {
            // given
            String input = " ";

            // when & then
            assertThatThrownBy(() -> Parser.parseResponse(input))
                    .isInstanceOf(StoreException.class)
                    .hasMessage("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
        }
    }
}
