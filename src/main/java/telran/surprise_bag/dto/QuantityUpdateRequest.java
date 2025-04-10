package telran.surprise_bag.dto;

import jakarta.validation.constraints.Min;

public record QuantityUpdateRequest(
		    @Min(1) int quantity
		) {

}
