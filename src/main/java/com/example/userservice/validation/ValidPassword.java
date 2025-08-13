package com.example.userservice.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface ValidPassword {
    String message() default "Password must have exactly one uppercase letter, exactly two digits, and be 8-12 characters long";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}