package tback.kicketingback.performance.exception.exceptions;

import java.util.List;

import lombok.Getter;
import tback.kicketingback.performance.domain.Seat;
import tback.kicketingback.performance.dto.SeatRowCol;

@Getter
public class AlreadySelectedSeatException extends RuntimeException {

	private final List<SeatRowCol> seatRowCol;

	private AlreadySelectedSeatException(List<SeatRowCol> seatRowCol) {
		super("이미 예약된 좌석");
		this.seatRowCol = seatRowCol;
	}

	public static AlreadySelectedSeatException of(List<Seat> seats) {
		List<SeatRowCol> rowCols = seats.stream()
			.map(seat -> new SeatRowCol(seat.getSeatRow(), seat.getSeatCol()))
			.toList();
		return new AlreadySelectedSeatException(rowCols);
	}
}
