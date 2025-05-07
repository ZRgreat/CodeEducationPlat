package sdu.codeeducationplat.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SingleChoiceValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface SingleChoiceConstraint {
    String message() default "单选题必须有且仅有一个正确选项";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}