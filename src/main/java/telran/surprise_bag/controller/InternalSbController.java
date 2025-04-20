package telran.surprise_bag.controller;

import static telran.surprise_bag.api.constants.ApiConstants.GET_SURPRISE_BAG_BY_ID;
import static telran.surprise_bag.api.constants.ApiConstants.SURPRISE_BAG_DEC;
import static telran.surprise_bag.api.constants.ApiConstants.SURPRISE_BAG_INC;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.surprise_bag.dto.QuantityUpdateRequest;
import telran.surprise_bag.dto.SurprisebagResponseDto;
import telran.surprise_bag.service.ISbService;

@RestController
@RequestMapping("/internal/surprise-bag")
@AllArgsConstructor
@Slf4j
public class InternalSbController {

	private final ISbService sbService;
	
	@GetMapping(GET_SURPRISE_BAG_BY_ID)
	public ResponseEntity<SurprisebagResponseDto> getSurpriseBagById(@PathVariable UUID id) {
		return ResponseEntity.ok(sbService.getById(id));
	}
	
		@PutMapping(SURPRISE_BAG_DEC)
		public ResponseEntity<Void> decrementAvailableCount(
				@PathVariable UUID id,
				@Valid @RequestBody QuantityUpdateRequest request) {
			sbService.decrementAvailableCount(id, request.quantity());
			return ResponseEntity.noContent().build();
		}

		@PutMapping(SURPRISE_BAG_INC)
		public ResponseEntity<Void> incrementAvailableCount(
				@PathVariable UUID id,
				@Valid @RequestBody QuantityUpdateRequest request) {
			sbService.incrementAvailableCount(id, request.quantity());
			return ResponseEntity.noContent().build();
		}
	
}
