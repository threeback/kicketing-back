package tback.kicketingback.performance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import tback.kicketingback.performance.domain.QReservation;
import tback.kicketingback.performance.domain.QSeat;
import tback.kicketingback.performance.dto.SeatGradeCount;

@Repository
public class ReservationRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	private final QReservation reservation;
	private final QSeat seat;

	public ReservationRepositoryCustom(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
		this.reservation = QReservation.reservation;
		this.seat = QSeat.seat;
	}

	public Optional<List<SeatGradeCount>> getUnorderedReservationsCountByGrade(Long onStageId) {
		List<SeatGradeCount> gradeCountList = queryFactory
			.select(Projections.constructor(SeatGradeCount.class,
				seat.grade,
				reservation.id.count()))
			.from(reservation)
			.join(reservation.seat, seat)
			.where(reservation.onStage.id.eq(onStageId)
				.and(reservation.orderNumber.isNull()))
			.groupBy(seat.grade)
			.fetch();

		return Optional.ofNullable(gradeCountList.isEmpty() ? null : gradeCountList);
	}
}
