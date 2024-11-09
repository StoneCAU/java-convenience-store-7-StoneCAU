package store.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import store.exception.ErrorMessage;
import store.exception.StoreException;

public class Parser {
    private final static String DELIMITER = ",";

    public static List<String> parseItems(String input) {
        validate(input);

        return Stream.of(input.replaceAll("[\\[\\]]", "").split(DELIMITER))
                .filter(item -> item.matches("[^\\-]+-\\d+"))
                .collect(Collectors.toList());
    }

    private static void validate(String input) {
        if (isNotMatch(input)) throw new StoreException(ErrorMessage.INVALID_FORMAT.getMessage());
    }

    private static boolean isNotMatch(String input) {
        return !input.matches("\\[[^\\-]+-\\d+](,\\[[^\\-]+-\\d+])*");
    }
}
