package tback.kicketingback.performance.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import tback.kicketingback.performance.domain.QOnStage;
import tback.kicketingback.performance.domain.QPerformance;
import tback.kicketingback.performance.domain.QPlace;
import tback.kicketingback.performance.domain.QReservation;
import tback.kicketingback.performance.domain.type.Genre;
import tback.kicketingback.performance.dto.GetPerformancesSize;
import tback.kicketingback.performance.dto.PlaceDTO;
import tback.kicketingback.performance.dto.Range;
import tback.kicketingback.performance.dto.SimplePerformanceDTO;
import tback.kicketingback.performance.dto.SimplePerformancePlaceDTO;

@Repository
public class PerformanceRepository {
	private final JPAQueryFactory queryFactory;
	private final QPerformance performance;
	private final QOnStage onStage;
	private final QReservation reservation;
	private final QPlace place;

	public PerformanceRepository(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
		this.performance = QPerformance.performance;
		this.onStage = QOnStage.onStage;
		this.reservation = QReservation.reservation;
		this.place = QPlace.place;
	}

	public List<SimplePerformancePlaceDTO> findGenreRankingPerformances(Genre genre, Range range,
		GetPerformancesSize getSize) {
		int size = getSize.getAnInt();

		return queryFactory.select(Projections.constructor(SimplePerformancePlaceDTO.class,
				Projections.constructor(SimplePerformanceDTO.class,
					performance.id,
					performance.genre,
					performance.name,
					performance.startDate,
					performance.endDate,
					performance.imageUrl),
				Projections.constructor(PlaceDTO.class,
					place.id,
					place.name,
					place.address,
					place.hall)))
			.from(onStage)
			.join(performance).on(onStage.performance.id.eq(performance.id)
				.and(onStage.dateTime.between(range.start().atStartOfDay(), range.end().atStartOfDay()))
				.and(performance.genre.eq(genre.getValue())))
			.join(place).on(place.id.eq(performance.place.id))
			.leftJoin(reservation).on(reservation.onStage.id.eq(onStage.id)
				.and(reservation.user.isNotNull()))
			.groupBy(performance)
			.orderBy(reservation.id.count().desc())
			.limit(size)
			.fetch();
	}

	public List<SimplePerformancePlaceDTO> findRankingPerformances(Range range, GetPerformancesSize getSize) {
		int size = getSize.getAnInt();

		return queryFactory.select(Projections.constructor(SimplePerformancePlaceDTO.class,
				Projections.constructor(SimplePerformanceDTO.class,
					performance.id,
					performance.genre,
					performance.name,
					performance.startDate,
					performance.endDate,
					performance.imageUrl),
				Projections.constructor(PlaceDTO.class,
					place.id,
					place.name,
					place.address,
					place.hall)))
			.from(onStage)
			.join(performance).on(onStage.performance.id.eq(performance.id)
				.and(onStage.dateTime.between(range.start().atStartOfDay(), range.end().atStartOfDay())))
			.join(place).on(place.id.eq(performance.place.id))
			.leftJoin(reservation).on(reservation.onStage.id.eq(onStage.id)
				.and(reservation.user.isNotNull()))
			.groupBy(performance)
			.orderBy(reservation.id.count().desc())
			.limit(size)
			.fetch();
	}
}
