package tback.kicketingback.performance.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import tback.kicketingback.performance.domain.type.Sex;

@Entity
@Getter
public class Star {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false)
	private LocalDate birthdate;

	@Column(nullable = false, length = 6)
	@Enumerated(EnumType.STRING)
	private Sex sex;

	@Column
	private String imageURL;
}
