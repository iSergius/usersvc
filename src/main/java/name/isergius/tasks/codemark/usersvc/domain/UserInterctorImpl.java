package name.isergius.tasks.codemark.usersvc.domain;

import name.isergius.tasks.codemark.usersvc.data.UserRepository;
import name.isergius.tasks.codemark.usersvc.model.User;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOG = LoggerFactory.getLogger(UserInterctorImpl.class);

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
            LOG.info("save user: %s", user);
            return userRepository.save(user)
                    .getId();
        } else {
            LOG.warn("user: %s have constraint violations: %s", user, violations);
            throw new ConstraintViolationException(violations);
        }
    }

    @Override
    public User get(long id) {
        User user = userRepository.findOne(id);
        LOG.info("getting user: %s", user);
        return user;
    }

    @Override
    public void delete(long id) {
        userRepository.delete(id);
        LOG.info("delete user by id: %d", id);
    }

    @Override
    public Page<User> list(Pageable pageable) {
        LOG.info("getting users: ");
        return userRepository.findAll(pageable);
    }

    @Override
    public void edit(User user) {
        if (userRepository.exists(user.getId())) {
            Set<ConstraintViolation<User>> violations = validator.validate(user);
            if (violations.isEmpty()) {
                LOG.info("user: %s is edited", user);
                userRepository.save(user);
            } else {
                LOG.warn("edited user: %o have constraint violations: %s", user, violations);
                throw new ConstraintViolationException(violations);
            }
        } else {
            //TODO refactor
            ConstraintViolation<User> violation = ConstraintViolationImpl.forBeanValidation(null,
                    null, "User is not exist", User.class, user, null, user, null, null, null, null);
            LOG.warn("user: %s is not exist");
            throw new ConstraintViolationException(Collections.singleton(violation));
        }
    }
}
