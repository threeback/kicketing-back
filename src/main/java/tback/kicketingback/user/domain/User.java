package tback.kicketingback.user.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false, length = 320)
	private String email;

	@Column(nullable = false, columnDefinition = "char(64)")
	private String password;

	@Column
	private String address;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, columnDefinition = "tinyint default 0")
	private Short state;

	@CreatedDate
	@Column(nullable = false)
	@Comment("생성일")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(nullable = false)
	@Comment("수정일")
	private LocalDateTime modifiedAt;
}
