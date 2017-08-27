package name.isergius.tasks.codemark.usersvc.domain;

import name.isergius.tasks.codemark.usersvc.data.UserRepository;
import name.isergius.tasks.codemark.usersvc.model.Role;
import name.isergius.tasks.codemark.usersvc.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Sergey Kondratyev
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserInterctorImplTest {

    private UserInterctorImpl interctor;

    @Mock
    private UserRepository repository;
    @Autowired
    private Validator validator;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        interctor = new UserInterctorImpl(repository, validator);
    }

    @Test
    public void testAdd_saveInRepository() throws Exception {
        long id = 1;
        User expectedUser = new User(id, "Admin", "adm", "Pass1", asList(new Role(id, "admin")));
        when(repository.save(expectedUser)).thenReturn(expectedUser);

        interctor.add(expectedUser);

        verify(repository).save(eq(expectedUser));
    }

    @Test
    public void testGet_gettingUserFromRepository() throws Exception {
        long id = 1;
        User expectedUser = new User(id, "Admin", "adm", "pass", asList(new Role(id, "admin")));

        User actualUser = interctor.get(id);

        verify(repository).findOne(id);
    }

    @Test
    public void testDelete_deletingUserFromRepository() throws Exception {
        long id = 1;

        interctor.delete(id);

        verify(repository).delete(id);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testAdd_validation() throws Exception {
        User user = new User(null, "admin", "secret", asList(new Role("admin")));

        interctor.add(user);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEdit_validation() throws Exception {
        long id = 1L;
        User user = new User(id, null, "admin", "secret", asList(new Role("admin")));
        when(repository.exists(id)).thenReturn(true);

        interctor.edit(user);
    }

}