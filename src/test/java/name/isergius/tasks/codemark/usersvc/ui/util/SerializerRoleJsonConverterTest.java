package name.isergius.tasks.codemark.usersvc.ui.util;

import name.isergius.tasks.codemark.usersvc.model.Role;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Sergey Kondratyev
 */
public class SerializerRoleJsonConverterTest {

    private SerializerRoleJsonConverter converter;

    @Before
    public void setUp() throws Exception {
        converter = new SerializerRoleJsonConverter();
    }

    @Test
    public void testConvert_success() throws Exception {
        long expectedId = 1L;
        Role role = new Role(expectedId, "ADMIN");
        long actualId = converter.convert(role);

        assertEquals(expectedId, actualId);
    }

}