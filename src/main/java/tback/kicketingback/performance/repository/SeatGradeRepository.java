package tback.kicketingback.performance.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import tback.kicketingback.performance.domain.SeatGrade;

public interface SeatGradeRepository extends JpaRepository<SeatGrade, Long> {

	List<SeatGrade> findSeatGradesByPerformanceId(UUID performanceUUID);
}
