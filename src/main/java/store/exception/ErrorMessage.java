package store.exception;

public enum ErrorMessage {
    INVALID_FORMAT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    INVALID_ITEM("존재하지 않는 상품입니다. 다시 입력해 주세요."),
    EXCEED_QUANTITY("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    INVALID_ITEM_QUANTITY("0 이상의 수량을 입력해주세요."),
    INVALID_RESPONSE("잘못된 입력입니다. 다시 입력해 주세요."),
    INVALID_FILE_CONTENT("유효하지 않은 파일 내용입니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
