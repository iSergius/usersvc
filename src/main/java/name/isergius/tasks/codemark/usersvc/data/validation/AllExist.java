package name.isergius.tasks.codemark.usersvc.data.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Sergey Kondratyev
 */

@Target(FIELD)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = AllExistValidator.class)
public @interface AllExist {

    String message() default "{name.isergius.tasks.codemark.usersvc.constraints.exist}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
