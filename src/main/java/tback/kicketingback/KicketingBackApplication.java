package tback.kicketingback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = RedisReactiveAutoConfiguration.class)
@EnableJpaAuditing
public class KicketingBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(KicketingBackApplication.class, args);
	}
}
