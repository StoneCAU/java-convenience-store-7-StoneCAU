package store.domain.product;

import store.domain.promotion.Promotion;

public class Product {
    private final String name;
    private final int price;
    private final String promotionName;
    private final Promotion promotion;
    private int quantity;

    public Product(String name, int price, int quantity, String promotionName, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotionName = promotionName;
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public Promotion getPromotion() {
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
        return "- " + name + " " + priceFormatting(price) + " 재고 없음 " + promotionName;
    }

    private String inStockFormatting() {
        return "- " + name + " " + priceFormatting(price) + " " + quantity + "개 " + promotionName;
    }

    private String priceFormatting(int price) {
        return String.format("%,d원", price);
    }

    private boolean isOutOfStock(int quantity) {
        return quantity <= 0;
    }
}
