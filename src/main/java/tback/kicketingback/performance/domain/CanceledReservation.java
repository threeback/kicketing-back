package tback.kicketingback.performance.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import tback.kicketingback.user.domain.User;

@Entity
@Getter
public class CanceledReservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

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

}
