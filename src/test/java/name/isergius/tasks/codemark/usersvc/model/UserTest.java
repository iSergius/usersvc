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
import static org.junit.Assert.assertTrue;

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
        User user = new User(null, "adm", "S2ecret", asList(new Role("ADMIN")));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
    }

    @Test
    public void testLogin_checkNotNullConstraint() throws Exception {
        User user = new User("Admin", null, "S1ecret", asList(new Role("ADMIN")));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
    }

    @Test
    public void testPassword_checkNotNullConstraint() throws Exception {
        User user = new User("Admin", "adm", null, asList(new Role("ADMIN")));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
    }

    @Test
    public void testPassword_checkUppercaseLatterConstraint() throws Exception {
        User user = new User("Admin", "adm", "t4tt", asList(new Role("ADMIN")));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.stream()
                .filter(uv -> uv.getMessageTemplate()
                        .equals("Password must contain min one latter and one digit"))
                .count());
    }

    @Test
    public void testPassword_checkDigitConstraint() throws Exception {
        User user = new User("Admin", "adm", "Ttt", asList(new Role("ADMIN")));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.stream()
                .filter(uv -> uv.getMessageTemplate()
                        .equals("Password must contain min one latter and one digit"))
                .count());
    }

    @Test
    public void testPassword_checkDigitAndLaterConstraintCaseOne() throws Exception {
        User user = new User("Admin", "adm", "tt3Ttt", asList(new Role("ADMIN")));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void testPassword_checkDigitAndLaterConstraintCaseTwo() throws Exception {
        User user = new User("Admin", "adm", "ttT3tt", asList(new Role("ADMIN")));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }
}