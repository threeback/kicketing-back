package tback.kicketingback.performance.dto;

import tback.kicketingback.performance.domain.Reservation;
import tback.kicketingback.performance.domain.Seat;

public record SeatReservationDTO(Seat seat, Reservation reservation) {
}
