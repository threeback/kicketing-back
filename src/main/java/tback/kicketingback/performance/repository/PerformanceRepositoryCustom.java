package tback.kicketingback.performance.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import tback.kicketingback.performance.domain.QOnStage;
import tback.kicketingback.performance.domain.QPerformance;
import tback.kicketingback.performance.domain.QPlace;
import tback.kicketingback.performance.domain.QReservation;
import tback.kicketingback.performance.domain.QStar;
import tback.kicketingback.performance.domain.QStarsIn;
import tback.kicketingback.performance.domain.type.Genre;
import tback.kicketingback.performance.dto.GetPerformancesSize;
import tback.kicketingback.performance.dto.PerformanceDTO;
import tback.kicketingback.performance.dto.PerformancePlaceDTO;
import tback.kicketingback.performance.dto.PlaceDTO;
import tback.kicketingback.performance.dto.Range;
import tback.kicketingback.performance.dto.SimplePerformanceDTO;
import tback.kicketingback.performance.dto.SimplePerformancePlaceDTO;
import tback.kicketingback.performance.dto.StarDTO;

@Repository
public class PerformanceRepositoryCustom {
	private final JPAQueryFactory queryFactory;
	private final QPerformance performance;
	private final QOnStage onStage;
	private final QReservation reservation;
	private final QPlace place;
	private final QStarsIn starsIn;
	private final QStar star;

	public PerformanceRepositoryCustom(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
		this.performance = QPerformance.performance;
		this.onStage = QOnStage.onStage;
		this.reservation = QReservation.reservation;
		this.place = QPlace.place;
		this.starsIn = QStarsIn.starsIn;
		this.star = QStar.star;
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

	public Optional<PerformancePlaceDTO> findPerformanceAndPlaceInfo(UUID performanceUUID) {
		return Optional.ofNullable(queryFactory.select(Projections.constructor(PerformancePlaceDTO.class,
				Projections.constructor(PerformanceDTO.class,
					performance.id,
					performance.name,
					performance.genre,
					performance.length,
					performance.startDate,
					performance.endDate,
					performance.ageLimit,
					performance.imageUrl,
					performance.place.id),
				Projections.constructor(PlaceDTO.class,
					place.id,
					place.name,
					place.address,
					place.hall)))
			.from(performance)
			.join(place).on(performance.place.id.eq(place.id))
			.where(performance.id.eq(performanceUUID))
			.fetchOne());
	}

	public List<StarDTO> findStarsIn(UUID performanceUUID) {
		return queryFactory.select(Projections.constructor(StarDTO.class,
				star.id,
				star.name,
				star.birthdate,
				star.sex,
				star.imageURL))
			.from(starsIn)
			.join(star).on(star.id.eq(starsIn.star.id))
			.where(starsIn.performance.id.eq(performanceUUID))
			.fetch();
	}

	public boolean isExistPerformance(UUID performanceUUID, Long onStageId) {
		Long count = queryFactory.select(onStage.id.count())
			.from(onStage)
			.where(onStage.id.eq(onStageId).and(onStage.performance.id.eq(performanceUUID)))
			.fetchOne();
		return count > 0;
	}
}
