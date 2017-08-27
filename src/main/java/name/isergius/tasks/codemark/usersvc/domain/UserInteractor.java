package name.isergius.tasks.codemark.usersvc.domain;

import name.isergius.tasks.codemark.usersvc.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Sergey Kondratyev
 */
public interface UserInteractor {

    long add(User user);

    User get(long id);

    void delete(long id);

    Page<User> list(Pageable pageable);

    void edit(User user);
}
