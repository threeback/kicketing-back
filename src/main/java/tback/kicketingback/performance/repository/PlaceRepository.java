package tback.kicketingback.performance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tback.kicketingback.performance.domain.Place;

public interface PlaceRepository extends JpaRepository<Place, Long> {

	Optional<Place> findById(Long id);
}
