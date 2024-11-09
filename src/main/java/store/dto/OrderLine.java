package store.dto;

import java.util.List;
import store.domain.product.Product;

public record OrderLine(List<Product> products, int quantity) {
}
