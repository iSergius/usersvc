package name.isergius.tasks.codemark.usersvc.domain;

import name.isergius.tasks.codemark.usersvc.model.User;

import java.util.List;

/**
 * Sergey Kondratyev
 */
public interface UserInteractor {

    void add(User user);

    User get(long id);

    void delete(long id);

    List<User> list();

    void edit(User user);
}
