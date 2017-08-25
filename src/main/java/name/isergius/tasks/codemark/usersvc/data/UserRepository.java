package name.isergius.tasks.codemark.usersvc.data;

import name.isergius.tasks.codemark.usersvc.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Sergey Kondratyev
 */
public interface UserRepository extends CrudRepository<User, Long> {

    User save(User entity);
}
