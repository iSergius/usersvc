package name.isergius.tasks.codemark.usersvc.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * Sergey Kondratyev
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
public class UserTest {

    @Autowired
    private Validator validator;

    @Test
    public void testName_checkNotNullConstraint() throws Exception {
        User user = new User(null, "adm", "secret", asList(new Role("ADMIN")));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
    }

    @Test
    public void testLogin_checkNotNullConstraint() throws Exception {
        User user = new User("Admin", null, "secret", asList(new Role("ADMIN")));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
    }
}