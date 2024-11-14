package store.domain.promotion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import store.util.FileLoader;

public class PromotionFactory {
    private static final String DELIMITER = ",";
    private static final String FILE_NAME = "promotions.md";

    public List<Promotion> initPromotions() {
        return FileLoader.loadMarkdownFile(FILE_NAME)
                .stream()
                .skip(1)
                .map(this::getFields)
                .map(this::create)
                .toList();
    }

    private Promotion create(List<String> fields) {
        final String name = fields.get(0);
        final int buy = Integer.parseInt(fields.get(1));
        final int get = Integer.parseInt(fields.get(2));
        final LocalDate start_date = dateFormatter(fields.get(3));
        final LocalDate end_date = dateFormatter(fields.get(4));

        return new Promotion(name, buy, get, start_date, end_date);
    }

    private List<String> getFields(String line) {
        return Arrays.stream(line.split(DELIMITER))
                .toList();
    }

    private LocalDate dateFormatter(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }

}
