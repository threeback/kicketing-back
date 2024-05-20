package tback.kicketingback.performance.repository;

import static tback.kicketingback.performance.domain.QOnStage.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
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
				reservation.id.count().coalesce(0L)))
			.from(seat)
			.leftJoin(reservation).on(
				reservation.seat.id.eq(seat.id)
					.and(reservation.onStage.id.eq(onStageId))
					.and(reservation.user.isNull().or(reservation.lockExpiredTime.after(LocalDateTime.now()))))
			.where(JPAExpressions.selectOne()
				.from(onStage)
				.where(onStage.id.eq(onStageId))
				.exists())
			.groupBy(seat.grade)
			.fetch();

		return Optional.ofNullable(gradeCountList.isEmpty() ? null : gradeCountList);
	}
}
