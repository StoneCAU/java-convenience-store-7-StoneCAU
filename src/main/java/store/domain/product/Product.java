package store.domain.product;

public class Product {
    final String name;
    final int price;
    final int quantity;
    final String promotion;

    public Product(String name, int price, int quantity, String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getPromotion() {
        return promotion;
    }

    @Override
    public String toString() {
        return formatting();
    }

    private String formatting() {
        if (isOutOfStock(quantity)) return outOfStockFormatting();
        return inStockFormatting();
    }

    private String outOfStockFormatting() {
        return "- " + name + " " + priceFormatting(price) + " 재고 없음 " + promotion;
    }

    private String inStockFormatting() {
        return "- " + name + " " + priceFormatting(price) + " " + quantity + "개 " + promotion;
    }

    private String priceFormatting(int price) {
        return String.format("%,d원", price);
    }

    private boolean isOutOfStock(int quantity) {
        return quantity <= 0;
    }
}
