package name.isergius.tasks.codemark.usersvc.domain;

import name.isergius.tasks.codemark.usersvc.data.UserRepository;
import name.isergius.tasks.codemark.usersvc.model.User;

/**
 * Sergey Kondratyev
 */
public class UserInterctorImpl implements UserInteractor {

    private UserRepository userRepository;

    public UserInterctorImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void add(User user) {
        userRepository.save(user);
    }
}