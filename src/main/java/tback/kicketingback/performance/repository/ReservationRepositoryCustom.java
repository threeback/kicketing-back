package tback.kicketingback.performance.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import tback.kicketingback.performance.domain.QOnStage;
import tback.kicketingback.performance.domain.QPerformance;
import tback.kicketingback.performance.domain.QPlace;
import tback.kicketingback.performance.domain.QReservation;
import tback.kicketingback.performance.domain.QSeat;
import tback.kicketingback.performance.dto.DetailReservationDTO;
import tback.kicketingback.performance.dto.OnStageDTO;
import tback.kicketingback.performance.dto.PlaceDTO;
import tback.kicketingback.performance.dto.SeatDTO;
import tback.kicketingback.performance.dto.SeatGradeCount;
import tback.kicketingback.performance.dto.SeatReservationDTO;
import tback.kicketingback.performance.dto.SimplePerformanceDTO;
import tback.kicketingback.performance.dto.SimpleReservationDTO;
import tback.kicketingback.performance.dto.SimpleSeatDTO;
import tback.kicketingback.user.domain.QUser;

@Repository
public class ReservationRepositoryCustom {
	private final JPAQueryFactory queryFactory;
	private final QPerformance performance;
	private final QOnStage onStage;
	private final QReservation reservation;
	private final QPlace place;
	private final QSeat seat;
	private final QUser user;

	public ReservationRepositoryCustom(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
		this.performance = QPerformance.performance;
		this.onStage = QOnStage.onStage;
		this.reservation = QReservation.reservation;
		this.place = QPlace.place;
		this.seat = QSeat.seat;
		this.user = QUser.user;
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
			.setLockMode(LockModeType.PESSIMISTIC_WRITE)
			.fetch();
	}

	public List<DetailReservationDTO> myReservations(Long userId) {

		return queryFactory.select(Projections.constructor(DetailReservationDTO.class,
				Projections.constructor(SimpleReservationDTO.class,
					reservation.id,
					reservation.orderedAt,
					reservation.orderNumber),
				Projections.constructor(OnStageDTO.class,
					onStage.dateTime,
					onStage.round),
				Projections.constructor(SeatDTO.class,
					seat.grade,
					seat.seatRow,
					seat.seatCol),
				Projections.constructor(SimplePerformanceDTO.class,
					performance.id,
					performance.name,
					performance.genre,
					performance.startDate,
					performance.endDate,
					performance.imageUrl),
				Projections.constructor(PlaceDTO.class,
					place.id,
					place.name,
					place.address,
					place.hall)))
			.from(reservation)
			.join(onStage).on(reservation.onStage.id.eq(onStage.id))
			.join(performance).on(onStage.performance.id.eq(performance.id))
			.join(seat).on(reservation.seat.id.eq(seat.id))
			.join(place).on(seat.place.id.eq(place.id))
			.where(reservation.user.id.eq(userId)
				.and(reservation.orderNumber.isNotNull()))
			.orderBy(onStage.dateTime.asc(),
				onStage.round.asc(),
				performance.name.asc())
			.fetch();
	}
}
