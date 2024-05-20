package tback.kicketingback.performance.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import tback.kicketingback.performance.domain.OnStage;

public interface OnStageRepository extends JpaRepository<OnStage, Long> {

	List<OnStage> findByPerformance_IdAndDateTimeBetween(UUID id, LocalDateTime startDate, LocalDateTime endDate);

	boolean existsByPerformance_Id(UUID performanceUUID);
}
