package telran.surprise_bag.model;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import telran.surprise_bag.dto.SurprisebagResponseDto;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "surprise_bag")
public class SurpriseBag {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private double price;

	@Column(nullable = false)
	private int availableCount;

	@Column(nullable = false)
	private boolean isAvailable;

	@Column(nullable = false)
	private LocalDateTime pickupTimeStart;

	@Column(nullable = false)
	private LocalDateTime pickupTimeEnd;

	@Column(name = "farmer_id", nullable = false)
	private UUID farmerId;

	@ElementCollection(fetch = FetchType.LAZY, targetClass = Size.class)
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "surprise_bag_sizes")
	@Column(name = "size", nullable = false)
	private Set<Size> size;

	@ElementCollection(fetch = FetchType.LAZY, targetClass = Category.class)
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "surprise_bag_categories")
	@Column(name = "category", nullable = false)
	private Set<Category> category;

	public SurpriseBag(UUID id) {
		this.id = id;
	}

	public static SurprisebagResponseDto buildFromEntity(SurpriseBag sb) {
		return SurprisebagResponseDto.builder()
				.id(sb.id)
				.category(sb.category)
				.size(sb.size)
				.availableCount(sb.availableCount)
				.description(sb.description)
				.price(sb.price)
				.pickupTimeStart(sb.pickupTimeStart)
				.pickupTimeEnd(sb.pickupTimeEnd)
				.build();
	}
}
