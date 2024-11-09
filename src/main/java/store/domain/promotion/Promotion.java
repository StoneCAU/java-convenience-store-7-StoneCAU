package store.domain.promotion;

public class Promotion {
    private final String name;
    private final int buy;
    private final int get;
    private final String start_date;
    private final String end_date;

    public Promotion(String name, int buy, int get, String start_date, String end_date) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.start_date = start_date;
        this.end_date = end_date;
    }
}
