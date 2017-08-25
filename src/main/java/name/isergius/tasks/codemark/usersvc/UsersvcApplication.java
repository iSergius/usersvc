package name.isergius.tasks.codemark.usersvc;

import name.isergius.tasks.codemark.usersvc.data.RoleRepository;
import name.isergius.tasks.codemark.usersvc.data.UserRepository;
import name.isergius.tasks.codemark.usersvc.domain.UserInteractor;
import name.isergius.tasks.codemark.usersvc.domain.UserInterctorImpl;
import name.isergius.tasks.codemark.usersvc.ui.UserController;
import name.isergius.tasks.codemark.usersvc.ui.util.DeserializerRoleJsonConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
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
	public UserInteractor userInteractor(UserRepository userRepository) {
		return new UserInterctorImpl(userRepository);
	}

    @Bean
    public DeserializerRoleJsonConverter deserializerRoleJsonConverter(RoleRepository roleRepository) {
        return new DeserializerRoleJsonConverter(roleRepository);
    }
}
