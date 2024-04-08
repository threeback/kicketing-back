package tback.kicketingback.global.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;
import tback.kicketingback.auth.interceptor.UserInterceptor;

@RequiredArgsConstructor
@Configuration
public class InterceptorAuthConfig implements WebMvcConfigurer {

	private final UserInterceptor userInterceptor;
	private final UserArgumentResolver userArgumentResolver;

	@Override
	public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(userArgumentResolver);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(userInterceptor)
			.order(1)
			.addPathPatterns("/**")
			.excludePathPatterns("/", "/api/user/sign-in", "/api/user/sign-up/*");
	}
}
