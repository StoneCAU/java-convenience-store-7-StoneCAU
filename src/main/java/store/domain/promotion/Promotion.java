package store.domain.promotion;

public class Promotion {
    private String name;
    private int buy;
    private int get;
    private String start_date;
    private String end_date;

    public Promotion(String name, int buy, int get, String start_date, String end_date) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public String getName() {
        return name;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }
}
