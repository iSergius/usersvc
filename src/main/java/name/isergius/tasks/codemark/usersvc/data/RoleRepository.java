package name.isergius.tasks.codemark.usersvc.data;

import name.isergius.tasks.codemark.usersvc.model.Role;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

/**
 * Sergey Kondratyev
 */
@CacheConfig(cacheNames = "roleCache")
public interface RoleRepository extends CrudRepository<Role, Long> {

    @Cacheable
    @Override
    Role findOne(Long aLong);
}
