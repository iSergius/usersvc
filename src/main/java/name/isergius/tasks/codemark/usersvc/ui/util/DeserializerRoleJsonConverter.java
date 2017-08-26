package name.isergius.tasks.codemark.usersvc.ui.util;


import com.fasterxml.jackson.databind.util.StdConverter;
import name.isergius.tasks.codemark.usersvc.data.RoleRepository;
import name.isergius.tasks.codemark.usersvc.model.Role;

/**
 * Sergey Kondratyev
 */
public class DeserializerRoleJsonConverter extends StdConverter<Long, Role> {

    private RoleRepository roleRepository;

    public DeserializerRoleJsonConverter(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role convert(Long value) {
        return roleRepository.findOne(value);
    }
}
