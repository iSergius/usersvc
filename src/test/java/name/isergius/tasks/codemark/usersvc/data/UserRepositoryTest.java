package name.isergius.tasks.codemark.usersvc.data;

import name.isergius.tasks.codemark.usersvc.model.Role;
import name.isergius.tasks.codemark.usersvc.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import static java.util.Arrays.asList;

/**
 * Sergey Kondratyev
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Test(expected = DataIntegrityViolationException.class)
    public void testSave_addUserWithIsNotExistRole() throws Exception {
        Role role = new Role(1, "OPER");
        User user = new User("Oper", "oper", "Secret1", asList(role));

        userRepository.save(user);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testSave_addUserWithIsNotCreatedRole() throws Exception {
        Role role = new Role(1, "OPER");
        User user = new User("Oper", "oper", "Secret1", asList(role));

        userRepository.save(user);
    }

}