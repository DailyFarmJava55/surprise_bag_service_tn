package telran.surprise_bag.controller;

import static telran.surprise_bag.api.constants.ApiConstants.*;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.surprise_bag.dto.QuantityUpdateRequest;
import telran.surprise_bag.dto.SurprisebagDto;
import telran.surprise_bag.dto.SurprisebagEditDto;
import telran.surprise_bag.dto.SurprisebagResponseDto;
import telran.surprise_bag.service.ISbService;

@RestController
@AllArgsConstructor
@Slf4j
public class SbController {

	private final ISbService sbService;

	@PostMapping(ADD_SURPRISE_BAG)
	public ResponseEntity<SurprisebagResponseDto> addSurpriseBag(
			@RequestParam UUID farmerId,
			@Valid @RequestBody SurprisebagDto surpriseBagDto) {
		return sbService.addSurpriseBag(farmerId, surpriseBagDto);
	}

	@PutMapping(UPDATE_SURPRISE_BAG + "/{bagId}")
	public ResponseEntity<SurprisebagResponseDto> updateSurpriseBag(
			@RequestParam UUID farmerId,
			@PathVariable UUID bagId,
			@Valid @RequestBody SurprisebagEditDto surpriseBagEditDto) {
		return sbService.updateSurpriseBag(farmerId, bagId, surpriseBagEditDto);
	}

	@GetMapping(GET_ALL_SURPRISE_BAGS_FOR_FARMER)
	public ResponseEntity<List<SurprisebagResponseDto>> getAvailableSurpriseBagForFarmer(
			@RequestParam UUID farmerId) {
		return sbService.getAvailableSurpriseBagForFarmer(farmerId);
	}

	@GetMapping(GET_ALL_SURPRISE_BAGS)
	public ResponseEntity<List<SurprisebagResponseDto>> getAllSurpriseBags() {
		return sbService.getAllSurpriseBags();
	}

	@DeleteMapping(DELETE_SURPRISE_BAG + "/{bagId}")
	public ResponseEntity<Void> deleteSurpriseBag(@PathVariable UUID bagId) {
		sbService.deleteSurpriseBag(bagId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping(GET_SURPRISE_BAG_BY_ID)
	public ResponseEntity<SurprisebagResponseDto> getSurpriseBagById(@PathVariable UUID id) {
		return ResponseEntity.ok(sbService.getById(id));
	}
//***********************************************************************
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
