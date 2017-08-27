package name.isergius.tasks.codemark.usersvc.domain;

import name.isergius.tasks.codemark.usersvc.data.UserRepository;
import name.isergius.tasks.codemark.usersvc.model.User;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Collections;
import java.util.Set;

/**
 * Sergey Kondratyev
 */
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UserInterctorImpl implements UserInteractor {

    private UserRepository userRepository;
    private Validator validator;

    public UserInterctorImpl(UserRepository userRepository, Validator validator) {
        this.userRepository = userRepository;
        this.validator = validator;
    }

    @Override
    public long add(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (violations.isEmpty()) {
            return userRepository.save(user)
                    .getId();
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
    public Page<User> list(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public void edit(User user) {
        if (userRepository.exists(user.getId())) {
            Set<ConstraintViolation<User>> violations = validator.validate(user);
            if (violations.isEmpty()) {
                userRepository.save(user);
            } else {
                throw new ConstraintViolationException(violations);
            }
        } else {
            //TODO refactor
            ConstraintViolation<User> violation = ConstraintViolationImpl.forBeanValidation(null,
                    null, "User is not exist", User.class, user, null, user, null, null, null, null);
            throw new ConstraintViolationException(Collections.singleton(violation));
        }
    }
}
