package tback.kicketingback.performance.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import tback.kicketingback.performance.domain.QReservation;
import tback.kicketingback.performance.domain.QSeat;
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

	public List<SimpleSeatDTO> findOnStageSeats(Long onStageId) {
		return queryFactory.select(Projections.constructor(SimpleSeatDTO.class,
				seat.id,
				seat.grade,
				seat.seatRow,
				seat.seatCol))
			.from(reservation)
			.join(seat).on(reservation.seat.id.eq(seat.id))
			.where(reservation.onStage.id.eq(onStageId)
				.and(reservation.user.id.isNull()
					.or(seat.lockExpiredTime.before(LocalDateTime.now()).and(reservation.orderNumber.isNull()))))
			.fetch();
	}
}
