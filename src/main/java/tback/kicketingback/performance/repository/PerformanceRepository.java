package tback.kicketingback.performance.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import tback.kicketingback.performance.domain.Performance;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {

	Optional<Performance> findById(UUID uuid);
}
