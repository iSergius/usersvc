package name.isergius.tasks.codemark.usersvc.ui.util;

import name.isergius.tasks.codemark.usersvc.data.RoleRepository;
import name.isergius.tasks.codemark.usersvc.model.Role;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Sergey Kondratyev
 */
@RunWith(MockitoJUnitRunner.class)
public class DeserializerRoleJsonConverterTest {

    private DeserializerRoleJsonConverter converter;
    @Mock
    private RoleRepository repository;

    private Role expectedRole;

    private long id = 1L;

    @Before
    public void setUp() throws Exception {
        converter = new DeserializerRoleJsonConverter(repository);
        expectedRole = new Role(id, "ADMIN");
        when(repository.exists(id)).thenReturn(true);
        when(repository.findOne(id)).thenReturn(expectedRole);
    }

    @Test
    public void testConvert_success() throws Exception {

        Role actualRole = converter.convert(id);

        assertEquals(expectedRole, actualRole);
    }

}