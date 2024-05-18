package tback.kicketingback.performance.dto;

import java.time.LocalDate;

import tback.kicketingback.performance.domain.type.Sex;

public record StarDTO(
	Long id,
	String name,
	LocalDate birthdate,
	Sex sex,
	String imageURL
) {

}
