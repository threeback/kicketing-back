package tback.kicketingback.performance.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import tback.kicketingback.performance.domain.type.DiscountType;
import tback.kicketingback.user.domain.User;

@Entity
@Getter
public class Reservation {

	@Id
	@UuidGenerator
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "on_stage_id", nullable = false)
	private OnStage onStage;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seat_id", nullable = false)
	private Seat seat;

	@Column
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	@Comment("예약일")
	private LocalDateTime orderedAt;

	@Column
	private String orderNumber;

	@Column
	@Enumerated(EnumType.STRING)
	private DiscountType discountType;
	
	@Column
	private LocalDateTime lockExpiredTime;

	protected Reservation() {
	}
}
