package telran.surprise_bag.controller;

import static telran.surprise_bag.api.constants.ApiConstants.ADD_SURPRISE_BAG;
import static telran.surprise_bag.api.constants.ApiConstants.DELETE_SURPRISE_BAG;
import static telran.surprise_bag.api.constants.ApiConstants.GET_ALL_SURPRISE_BAGS;
import static telran.surprise_bag.api.constants.ApiConstants.GET_ALL_SURPRISE_BAGS_FOR_FARMER;
import static telran.surprise_bag.api.constants.ApiConstants.UPDATE_SURPRISE_BAG;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
	@PreAuthorize("hasRole('FARMER')")
	public ResponseEntity<SurprisebagResponseDto> addSurpriseBag(
			@RequestHeader("x-user-id") UUID farmerId,
			@Valid @RequestBody SurprisebagDto surpriseBagDto) {
		return sbService.addSurpriseBag(farmerId, surpriseBagDto);
	}

	@PutMapping(UPDATE_SURPRISE_BAG + "/{bagId}")
	@PreAuthorize("hasRole('FARMER')")
	public ResponseEntity<SurprisebagResponseDto> updateSurpriseBag(
			@RequestHeader("x-user-id") UUID farmerId,
			@PathVariable UUID bagId,
			@Valid @RequestBody SurprisebagEditDto surpriseBagEditDto) {
		return sbService.updateSurpriseBag(farmerId, bagId, surpriseBagEditDto);
	}

	@GetMapping(GET_ALL_SURPRISE_BAGS_FOR_FARMER)
	@PreAuthorize("hasRole('FARMER')")
	public ResponseEntity<List<SurprisebagResponseDto>> getAvailableSurpriseBagForFarmer(
			@RequestHeader("x-user-id") UUID farmerId) {
		return sbService.getAvailableSurpriseBagForFarmer(farmerId);
	}

	@GetMapping(GET_ALL_SURPRISE_BAGS)
	public ResponseEntity<List<SurprisebagResponseDto>> getAllSurpriseBags() {
		return sbService.getAllSurpriseBags();
	}

	@DeleteMapping(DELETE_SURPRISE_BAG + "/{bagId}")
	@PreAuthorize("hasRole('FARMER')")
	public ResponseEntity<Void> deleteSurpriseBag(@PathVariable UUID bagId,
			@RequestHeader("x-user-id") UUID farmerId) {
		sbService.deleteSurpriseBag(bagId, farmerId);
		return ResponseEntity.noContent().build();
	}


}
