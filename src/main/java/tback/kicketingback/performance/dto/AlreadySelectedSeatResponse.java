package tback.kicketingback.performance.dto;

import java.util.List;

public record AlreadySelectedSeatResponse(String errorMessage, List<SeatRowCol> seats) {
}
