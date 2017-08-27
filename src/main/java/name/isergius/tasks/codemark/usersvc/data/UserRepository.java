package name.isergius.tasks.codemark.usersvc.data;

import name.isergius.tasks.codemark.usersvc.model.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * Sergey Kondratyev
 */
@CacheConfig(cacheNames = "usersCache")
public interface UserRepository extends CrudRepository<User, Long> {

    @Cacheable
    @Override
    User save(User entity);

    @Cacheable
    @Override
    User findOne(Long id);

    @Override
    void delete(Long id);

    @Cacheable
    Page<User> findAll(Pageable pageable);
}
