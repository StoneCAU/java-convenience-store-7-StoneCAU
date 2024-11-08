package store.dto;

import java.util.List;

public record Order(List<OrderLine> orderLines) {
}
