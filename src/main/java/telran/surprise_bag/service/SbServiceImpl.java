package telran.surprise_bag.service;

import static telran.surprise_bag.api.messages.ErrorMessages.SURPRISE_BAG_NOT_FOUND;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.surprise_bag.dao.SurpriseBagRepository;
import telran.surprise_bag.dto.SurprisebagDto;
import telran.surprise_bag.dto.SurprisebagEditDto;
import telran.surprise_bag.dto.SurprisebagResponseDto;
import telran.surprise_bag.model.SurpriseBag;

@Service
@Slf4j
@RequiredArgsConstructor
public class SbServiceImpl implements ISbService {

	private final SurpriseBagRepository sbRepository;

	@Override
	@Transactional
	public ResponseEntity<SurprisebagResponseDto> addSurpriseBag(UUID farmerId, @Valid SurprisebagDto dto) {
		log.info("Creating SurpriseBag for Farmer ID: {}", farmerId);

		SurpriseBag sb = SurpriseBag.builder()
				.availableCount(dto.getAvailableCount())
				.description(dto.getDescription())
				.price(dto.getPrice())
				.farmerId(farmerId)
				.category(dto.getCategories())
				.size(dto.getSize())
				.pickupTimeStart(dto.getPickupTimeStart())
				.pickupTimeEnd(dto.getPickupTimeEnd())
				.isAvailable(dto.getAvailableCount() > 0)
				.build();

		sbRepository.save(sb);
		log.info("SurpriseBag created: {}", sb.getId());

		return ResponseEntity.status(HttpStatus.CREATED).body(SurprisebagResponseDto.of(sb));
	}

	@Override
	public ResponseEntity<List<SurprisebagResponseDto>> getAvailableSurpriseBagForFarmer(UUID farmerId) {
		log.info("Getting SurpriseBags for Farmer ID: {}", farmerId);

		List<SurprisebagResponseDto> result = sbRepository.findAll().stream()
				.filter(bag -> farmerId.equals(bag.getFarmerId()))
				.map(SurpriseBag::buildFromEntity)
				.toList();

		return ResponseEntity.ok(result);
	}

	@Override
	public ResponseEntity<List<SurprisebagResponseDto>> getAllSurpriseBags() {
		List<SurprisebagResponseDto> result = sbRepository.findAllSurpriseBags().stream()
				.map(SurprisebagResponseDto::of)
				.toList();

		return ResponseEntity.ok(result);
	}

	@Override
	public ResponseEntity<SurprisebagResponseDto> updateSurpriseBag(UUID farmerId, UUID bagId,
			@Valid SurprisebagEditDto dto) {

		log.info("Updating SurpriseBag ID: {} for Farmer ID: {}", bagId, farmerId);

		SurpriseBag sb = sbRepository.findById(bagId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, SURPRISE_BAG_NOT_FOUND));

		if (!sb.getFarmerId().equals(farmerId)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,
					"You are not allowed to update this Surprise Bag");
		}

		if (dto.getAvailableCount() != 0) {
			sb.setAvailableCount(dto.getAvailableCount());
			sb.setAvailable(dto.getAvailableCount() > 0);
		}
		if (dto.getDescription() != null) sb.setDescription(dto.getDescription());
		if (dto.getPrice() != 0) sb.setPrice(dto.getPrice());
		if (dto.getCategories() != null) sb.setCategory(dto.getCategories());
		if (dto.getSize() != null) sb.setSize(dto.getSize());
		if (dto.getPickupTimeStart() != null) sb.setPickupTimeStart(dto.getPickupTimeStart());
		if (dto.getPickupTimeEnd() != null) sb.setPickupTimeEnd(dto.getPickupTimeEnd());

		sbRepository.save(sb);
		log.info("SurpriseBag {} updated", sb.getId());

		return ResponseEntity.ok(SurprisebagResponseDto.of(sb));
	}

	@Override
	public void deleteSurpriseBag(UUID bagId) {
		if (!sbRepository.existsById(bagId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, SURPRISE_BAG_NOT_FOUND + bagId);
		}
		sbRepository.deleteById(bagId);
		log.info("SurpriseBag {} deleted", bagId);
	}
//************************************************************************************
	@Override
	public SurprisebagResponseDto getById(UUID id) {
		SurpriseBag sb = sbRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, SURPRISE_BAG_NOT_FOUND));
		return SurpriseBag.buildFromEntity(sb);
	}

	@Override
	@Transactional
	public void incrementAvailableCount(UUID id, int quantity) {
		SurpriseBag sb = sbRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, SURPRISE_BAG_NOT_FOUND));
		sb.setAvailableCount(sb.getAvailableCount() + quantity);
		sbRepository.save(sb);
	}

	@Override
	@Transactional
	public void decrementAvailableCount(UUID id, int quantity) {
		SurpriseBag sb = sbRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, SURPRISE_BAG_NOT_FOUND));

		if (sb.getAvailableCount() < quantity) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No more surprise bags available");
		}
		sb.setAvailableCount(sb.getAvailableCount() - quantity);
		sbRepository.save(sb);
	}
}
