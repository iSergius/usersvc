package name.isergius.tasks.codemark.usersvc.domain;

import name.isergius.tasks.codemark.usersvc.data.UserRepository;
import name.isergius.tasks.codemark.usersvc.model.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.eq;

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

        Mockito.verify(repository).save(eq(expectedUser));
    }

}