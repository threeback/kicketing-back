package tback.kicketingback.performance.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import tback.kicketingback.performance.domain.Performance;
import tback.kicketingback.performance.domain.QOnStage;
import tback.kicketingback.performance.domain.QPerformance;
import tback.kicketingback.performance.domain.QReservation;
import tback.kicketingback.performance.domain.type.Genre;
import tback.kicketingback.performance.dto.DateUnit;
import tback.kicketingback.performance.dto.GetPerformancesSize;
import tback.kicketingback.performance.dto.Range;

@Repository
public class PerformanceRepository {
	private final JPAQueryFactory queryFactory;
	private final QPerformance performance;
	private final QOnStage onStage;
	private final QReservation reservation;

	public PerformanceRepository(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
		this.performance = QPerformance.performance;
		this.onStage = QOnStage.onStage;
		this.reservation = QReservation.reservation;
	}

	public List<Performance> getGenreRankingPerformances(Genre genre, DateUnit dateUnit, GetPerformancesSize getSize) {
		Range range = dateUnit.getRangeSupplier().get();
		int size = getSize.getAnInt();

		return queryFactory.select(performance)
			.from(onStage)
			.join(performance).on(onStage.performance.id.eq(performance.id))
			.leftJoin(reservation).on(reservation.onStage.id.eq(onStage.id))
			.where(onStage.dateTime.between(range.start().atStartOfDay(), range.end().atStartOfDay())
				.and(performance.genre.eq(genre.getValue())))
			.groupBy(performance)
			.orderBy(reservation.id.count().desc())
			.limit(size)
			.fetch();
	}

	public List<Performance> getRankingPerformances(DateUnit dateUnit, GetPerformancesSize getSize) {
		Range range = dateUnit.getRangeSupplier().get();
		int size = getSize.getAnInt();

		return queryFactory.select(performance)
			.from(onStage)
			.join(performance).on(onStage.performance.id.eq(performance.id))
			.leftJoin(reservation).on(reservation.onStage.id.eq(onStage.id))
			.where(onStage.dateTime.between(range.start().atStartOfDay(), range.end().atStartOfDay()))
			.groupBy(performance)
			.orderBy(reservation.id.count().desc())
			.limit(size)
			.fetch();
	}
}
