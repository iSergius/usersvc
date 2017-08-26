package name.isergius.tasks.codemark.usersvc.domain;

import name.isergius.tasks.codemark.usersvc.data.UserRepository;
import name.isergius.tasks.codemark.usersvc.model.Role;
import name.isergius.tasks.codemark.usersvc.model.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Sergey Kondratyev
 */
public class UserInterctorImplTest {

    private UserInterctorImpl interctor;

    @Mock
    private UserRepository repository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        interctor = new UserInterctorImpl(repository);
    }

    @Test
    public void testAdd_saveInRepository() throws Exception {
        User expectedUser = new User();

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
}