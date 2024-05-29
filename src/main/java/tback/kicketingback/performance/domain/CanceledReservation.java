package tback.kicketingback.performance.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import tback.kicketingback.performance.domain.type.Grade;
import tback.kicketingback.performance.dto.ReservationDTO;
import tback.kicketingback.user.domain.User;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class CanceledReservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreatedDate
	@Column(nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	@Comment("취소일")
	private LocalDateTime canceledAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	//예매 정보
	@Column(nullable = false)
	private String orderNumber;

	@Column(nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	@Comment("예약일")
	private LocalDateTime orderedAt;

	//공연 정보
	@Column(nullable = false)
	private String performanceName;

	@Column(nullable = false)
	private String imageUrl;

	@Column(nullable = false)
	@Comment("공연일")
	private LocalDateTime performanceDate;

	@Column(nullable = false)
	private int round;

	@Column(nullable = false)
	private String placeName;

	@Column(nullable = false)
	private String hall;

	// 좌석 정보
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Grade grade;

	@Column(nullable = false)
	private String seatRow;

	@Column(nullable = false)
	private int seatCol;

	protected CanceledReservation() {
	}

	private CanceledReservation(
		User user,
		String orderNumber, LocalDateTime orderedAt,
		String performanceName, String imageUrl, LocalDateTime performanceDate,
		int round, String placeName, String hall,
		Grade grade, String seatRow, int seatCol) {
		this.user = user;
		this.orderNumber = orderNumber;
		this.orderedAt = orderedAt;
		this.performanceName = performanceName;
		this.imageUrl = imageUrl;
		this.performanceDate = performanceDate;
		this.round = round;
		this.placeName = placeName;
		this.hall = hall;
		this.grade = grade;
		this.seatRow = seatRow;
		this.seatCol = seatCol;
	}

	public static CanceledReservation of(
		ReservationDTO reservation,
		User user,
		Performance performance,
		OnStage onStage,
		Place place,
		Seat seat
	) {
		return new CanceledReservation(
			user,
			reservation.getOrderNumber(), reservation.getOrderedAt(),
			performance.getName(), performance.getImageUrl(),
			onStage.getDateTime(), onStage.getRound(), place.getName(),
			place.getHall(),
			seat.getGrade(), seat.getSeatRow(), seat.getSeatCol()
		);
	}
}
