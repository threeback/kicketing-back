package tback.kicketingback.performance.exception.exceptions;

import java.util.List;

import lombok.Getter;
import tback.kicketingback.performance.dto.ReservationDTO;

@Getter
public class InvalidReservationDataException extends RuntimeException {

	private final List<ReservationDTO> reservations;

	public InvalidReservationDataException(List<ReservationDTO> reservations) {
		super("예매 취소 불가");
		this.reservations = reservations;
	}
}
