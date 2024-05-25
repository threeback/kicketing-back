package tback.kicketingback.performance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tback.kicketingback.performance.domain.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	List<Reservation> findReservationByOrderNumber(String OrderNumber);
}
