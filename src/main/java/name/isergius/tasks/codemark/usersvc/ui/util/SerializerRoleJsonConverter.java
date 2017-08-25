package name.isergius.tasks.codemark.usersvc.ui.util;


import com.fasterxml.jackson.databind.util.StdConverter;
import name.isergius.tasks.codemark.usersvc.model.Role;

/**
 * Sergey Kondratyev
 */
public class SerializerRoleJsonConverter extends StdConverter<Role, Long> {

    @Override
    public Long convert(Role value) {
        return value.getId();
    }
}
