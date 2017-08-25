package name.isergius.tasks.codemark.usersvc;

import name.isergius.tasks.codemark.usersvc.domain.UserInteractor;
import name.isergius.tasks.codemark.usersvc.domain.UserInterctorImpl;
import name.isergius.tasks.codemark.usersvc.ui.UserController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UsersvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersvcApplication.class, args);
	}

	@Bean
	public UserController userController(UserInteractor userInteractor) {
		return new UserController(userInteractor);
	}

	@Bean
	public UserInteractor userInteractor() {
		return new UserInterctorImpl();
	}
}
