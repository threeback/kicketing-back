package tback.kicketingback.performance.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import tback.kicketingback.performance.domain.QReservation;
import tback.kicketingback.performance.domain.QSeat;
import tback.kicketingback.performance.dto.SeatReservationDTO;
import tback.kicketingback.performance.dto.SimpleSeatDTO;
import tback.kicketingback.user.domain.QUser;

@Repository
public class ReservationRepositoryCustom {
	private final JPAQueryFactory queryFactory;
	private final QReservation reservation;
	private final QSeat seat;
	private final QUser user;

	public ReservationRepositoryCustom(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
		this.reservation = QReservation.reservation;
		this.seat = QSeat.seat;
		this.user = QUser.user;
	}

	public Optional<List<SimpleSeatDTO>> findOnStageSeats(Long onStageId) {
		List<SimpleSeatDTO> seatDTOS = queryFactory.select(Projections.constructor(SimpleSeatDTO.class,
				seat.id,
				seat.grade,
				seat.seatRow,
				seat.seatCol))
			.from(reservation)
			.join(seat).on(reservation.seat.id.eq(seat.id))
			.where(reservation.onStage.id.eq(onStageId)
				.and(reservation.orderNumber.isNull()
					.and(reservation.user.id.isNull()
						.or(reservation.lockExpiredTime.isNotNull()
							.and(reservation.lockExpiredTime.before(LocalDateTime.now()))))))
			.fetch();
		return Optional.ofNullable(seatDTOS.isEmpty() ? null : seatDTOS);
	}

	public List<SeatReservationDTO> findSeats(Long onStageId, List<Long> seatsIds) {
		return queryFactory.select(
				Projections.constructor(SeatReservationDTO.class,
					seat, reservation))
			.from(reservation)
			.join(seat).on(reservation.seat.id.eq(seat.id)
				.and(seat.id.in(seatsIds)))
			.where(reservation.onStage.id.eq(onStageId))
			.fetch();
	}
}
