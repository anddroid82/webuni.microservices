package hu.webuni.login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import hu.webuni.security.config.JwtAuthFilter;
import hu.webuni.security.config.JwtService;
import hu.webuni.security.config.SecurityConfig;

@SpringBootApplication
@Import({SecurityConfig.class,JwtService.class,JwtAuthFilter.class})
public class LoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginApplication.class, args);
	}

}
