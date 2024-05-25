package tback.kicketingback.performance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tback.kicketingback.performance.domain.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long> {

	Optional<Seat> findById(Long id);
}
