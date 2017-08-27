package name.isergius.tasks.codemark.usersvc.data.validation;

import name.isergius.tasks.codemark.usersvc.model.Role;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

/**
 * Sergey Kondratyev
 */
public class AllExistValidator implements ConstraintValidator<AllExist, Collection<Role>> {

    private Role wrongRole;
    private String messageTemplate;

    @Override
    public void initialize(AllExist constraintAnnotation) {
        this.messageTemplate = constraintAnnotation.message();
        this.wrongRole = new Role();
    }

    @Override
    public boolean isValid(Collection<Role> value, ConstraintValidatorContext context) {
        boolean match = value.stream()
                .anyMatch(role -> role.equals(wrongRole));
        if (match) {
            context.buildConstraintViolationWithTemplate(messageTemplate);
        }
        return !match;
    }
}
