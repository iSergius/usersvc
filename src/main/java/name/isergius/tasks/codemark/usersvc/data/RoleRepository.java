package name.isergius.tasks.codemark.usersvc.data;

import name.isergius.tasks.codemark.usersvc.model.Role;
import org.springframework.data.repository.CrudRepository;

/**
 * Sergey Kondratyev
 */
public interface RoleRepository extends CrudRepository<Role, Long> {

    @Override
    Role findOne(Long aLong);
}
