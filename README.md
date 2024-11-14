# 기능 요구 사항

### 결제 시스템

- **구매자**의 **할인 혜택**과 **재고 상황**을 고려하여 **최종 결제 금액**을 계산하고 안내하는 **결제 시스템**을 구현한다.
- 사용자가 입력한 상품의 **가격**과 **수량**을 기반으로 **최종 결제 금액**을 계산한다.
    - **총구매액**은 상품별 가격과 수량을 곱하여 계산하며, **프로모션 및 멤버십 할인 정책**을 반영하여 **최종 결제 금액**을 산출한다.
- **구매 내역**과 산출한 **금액 정보**를 **영수증**으로 출력한다.
- **영수증** 출력 후 **추가 구매**를 진행할지 또는 종료할지를 선택할 수 있다.
- 사용자가 잘못된 값을 입력할 경우 **IllegalArgumentException**를 발생시키고, "[ERROR]"로 시작하는 에러 메시지를 출력 후 그 부분부터 입력을 다시 받는다.
    - Exception이 아닌 **IllegalArgumentException**, **IllegalStateException** 등과 같은 명확한 유형을 처리한다.

### 재고 관리

- 각 상품의 **재고 수량**을 고려하여 결제 가능 여부를 확인한다.
- 고객이 상품을 구매할 때마다, 결제된 수량만큼 해당 상품의 재고에서 차감하여 수량을 관리한다.
- 재고를 차감함으로써 시스템은 최신 재고 상태를 유지하며, 다음 고객이 구매할 때 정확한 재고 정보를 제공한다.

### 프로모션 할인

- 오늘 날짜가 **프로모션 기간** 내에 포함된 경우에만 할인을 적용한다.
- 프로모션은 **N개 구매 시 1개 무료 증정(Buy N Get 1 Free)의 형태**로 진행된다.
- 1+1 또는 2+1 프로모션이 각각 지정된 상품에 적용되며, 동일 상품에 여러 프로모션이 적용되지 않는다.
- 프로모션 혜택은 **프로모션 재고** 내에서만 적용할 수 있다.
- 프로모션 기간 중이라면 프로모션 재고를 우선적으로 차감하며, 프로모션 재고가 부족할 경우에는 **일반 재고**를 사용한다.
- 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 필요한 수량을 추가로 가져오면 혜택을 받을 수 있음을 안내한다.
- 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제하게 됨을 안내한다.

### 멤버십 할인

- 멤버십 회원은 **프로모션 미적용 금액의 30%를 할인**받는다.
- **프로모션 적용 후 남은 금액**에 대해 멤버십 할인을 적용한다.
- 멤버십 할인의 **최대 한도**는 8,000원이다.

### 영수증 출력

- 영수증은 고객의 **구매 내역**과 **할인**을 요약하여 출력한다.
- 영수증 항목은 아래와 같다.
    - 구매 상품 내역: 구매한 상품명, 수량, 가격
    - 증정 상품 내역: 프로모션에 따라 무료로 제공된 증정 상품의 목록
- 금액 정보
    - 총구매액: 구매한 상품의 총 수량과 총 금액
    - 행사할인: 프로모션에 의해 할인된 금액
    - 멤버십할인: 멤버십에 의해 추가로 할인된 금액
    - 내실돈: 최종 결제 금액
- 영수증의 구성 요소를 보기 좋게 정렬하여 고객이 쉽게 금액과 수량을 확인할 수 있게 한다.

# 구현할 기능 목록

### 입력

- [x] 구매할 상품과 수량을 입력 받는다
    - "[상품명-수량]"과 같은 형식을 따른다
- [x] 프로모션 적용이 가능한 상품인데, 고객이 해당 수량보다 적게 가져오면 추가 여부를 입력받는다(Y/N)
- [x] 프로모션 상품이나 프로모션 재고가 없는 경우, 일부 수량에 대해 정가 결제 여부를 입력받는다(Y/N)
- [x] 멤버십 할인 적용 여부를 입력 받는다(Y/N)
- [x] 추가 구매 여부를 입력 받는다(Y/N)

### 출력

- [x] 환영 인사와 상품명, 프로모션, 재고를 출력
    - [x] 재고가 0개라면 **재고 없음** 출력
- [x] 프로모션 적용이 가능한 상품에 고객이 해당 수량보다 적게 가져왔을 경우 메시지 출력
- [x] 프로모션 재고가 부족하여 일부 상품에 대해 정가 결제 여부를 묻는 메세지 출력
- [x] 멤버십 할인 적용 여부 메시지 출력
- [x] 구매 상품 내역, 증정 상품 내역, 금액 정보를 출력
- [x] 추가 구매 여부를 확인하는 메시지 출력

# 예외 처리

### 입력

- 구매할 상품과 수량 입력
    - [x] "[상품명-수량]"에 맞지 않는 형식일 경우
    - [x] 구매할 상품이 존재하지 않는 경우
    - [x] 구매 수량이 재고 수량을 초과한 경우
    - [x] 구매 수량을 0으로 입력한 경우
    - [x] 중복된 상품을 입력한 경우
- 프로모션 적용 여부, 멤버십 할인 여부, 추가 구매 여부 입력
    - [x] Y 또는 N 이외의 입력일 경우

# 프로그래밍 요구 사항

### main

- [x] indent(인덴트, 들여쓰기) depth를 3이 넘지 않도록 구현한다
- [x] 3항 연산자를 쓰지 않는다.
- [x] 함수(또는 메서드)가 한 가지 일만 하도록 최대한 작게 만들어라
- [x] else 예약어를 쓰지 않는다
- [x] Java Enum을 적용하여 프로그램을 구현한다
- [x] 함수(또는 메서드)의 길이가 10라인을 넘어가지 않도록 구현한다
- [x] 입출력을 담당하는 클래스를 별도로 구현한다
    - 주어진 InputView, OutputView 클래스를 참고하여 입출력 클래스를 구현한다
    - 클래스 이름, 메소드 반환 유형, 시그니처 등은 자유롭게 수정할 수 있다

### 라이브러리

- [x] camp.nextstep.edu.missionutils에서 제공하는 DateTimes 및 Console API를 사용하여 구현해야 한다
    - 현재 날짜와 시간을 가져오려면 camp.nextstep.edu.missionutils.DateTimes의 now()를 활용한다
    - 사용자가 입력하는 값은 camp.nextstep.edu.missionutils.Console의 readLine()을 활용한다

### test

- [x] JUnit 5와 AssertJ를 이용하여 정리한 기능 목록이 정상적으로 작동하는지 테스트 코드로 확인한다
- [x] 구현한 기능에 대한 단위 테스트를 작성한다
    - 단, UI(System.out, System.in, Scanner) 로직은 제외한다