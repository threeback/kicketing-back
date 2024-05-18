package tback.kicketingback.performance.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.performance.dto.SeatGradeCount;
import tback.kicketingback.performance.exception.exceptions.InvalidOnStageIDException;
import tback.kicketingback.performance.repository.ReservationRepositoryCustom;

@Service
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepositoryCustom reservationRepositoryCustom;

	@Transactional(readOnly = true)
	public List<SeatGradeCount> getUnorderedReservationsCountByGrade(Long onStageId) {
		return reservationRepositoryCustom.getUnorderedReservationsCountByGrade(onStageId)
			.orElseThrow(() -> new InvalidOnStageIDException(onStageId));
	}
}
