package name.isergius.tasks.codemark.usersvc.domain;

import name.isergius.tasks.codemark.usersvc.data.UserRepository;
import name.isergius.tasks.codemark.usersvc.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

/**
 * Sergey Kondratyev
 */
public class UserInterctorImpl implements UserInteractor {

    private UserRepository userRepository;
    private Validator validator;

    public UserInterctorImpl(UserRepository userRepository, Validator validator) {
        this.userRepository = userRepository;
        this.validator = validator;
    }

    @Override
    public void add(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (violations.isEmpty()) {
            userRepository.save(user);
        } else {
            throw new ConstraintViolationException(violations);
        }
    }

    @Override
    public User get(long id) {
        return userRepository.findOne(id);
    }

    @Override
    public void delete(long id) {
        userRepository.delete(id);
    }

    @Override
    public List<User> list() {
        return userRepository.findAll();
    }

    @Override
    public void edit(User user) {
        if (userRepository.exists(user.getId())) {
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User is not exist");
        }
    }
}
