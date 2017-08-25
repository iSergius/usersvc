package name.isergius.tasks.codemark.usersvc.domain;

import name.isergius.tasks.codemark.usersvc.model.User;

/**
 * Sergey Kondratyev
 */
public interface UserInteractor {

    void add(User user);

    User get(long id);
}
