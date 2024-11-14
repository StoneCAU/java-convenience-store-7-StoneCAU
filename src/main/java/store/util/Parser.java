package store.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import store.exception.ErrorMessage;
import store.exception.StoreException;

public class Parser {
    private final static String DELIMITER = ",";
    private final static String VALIDATE_DELIMITER = "-";

    public static List<String> parseItems(String input) {
        validatePurchaseInput(input);

        List<String> items = Stream.of(input.replaceAll("[\\[\\]]", "").split(DELIMITER))
                .filter(item -> item.matches("[^\\-]+-\\d+"))
                .collect(Collectors.toList());

        validateDuplicate(items);

        return items;
    }

    public static boolean parseResponse(String input) {
        validateResponse(input);

        return input.equals("Y");
    }

    private static void validateDuplicate(List<String> items) {
        Set<String> uniqueNames = new HashSet<>();

        items.stream()
                .map(item -> item.split(VALIDATE_DELIMITER)[0]) // Extract product name
                .forEach(name -> {
                    if (!uniqueNames.add(name)) {
                        throw new StoreException(ErrorMessage.DUPLICATED_ITEM.getMessage());
                    }
                });
    }

    private static void validateResponse(String input) {
        if (!input.equals("Y") && !input.equals("N")) {
            throw new StoreException(ErrorMessage.INVALID_RESPONSE.getMessage());
        }
    }

    private static void validatePurchaseInput(String input) {
        if (isNotMatch(input)) {
            throw new StoreException(ErrorMessage.INVALID_FORMAT.getMessage());
        }
    }

    private static boolean isNotMatch(String input) {
        return !input.matches("\\[[^\\-]+-\\d+](,\\[[^\\-]+-\\d+])*");
    }
}
