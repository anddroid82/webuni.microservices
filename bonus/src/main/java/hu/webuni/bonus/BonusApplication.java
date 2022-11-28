package hu.webuni.bonus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import hu.webuni.security.config.SecurityConfig;

@SpringBootApplication
@Import({SecurityConfig.class})
public class BonusApplication {

	public static void main(String[] args) {
		SpringApplication.run(BonusApplication.class, args);
	}

}
