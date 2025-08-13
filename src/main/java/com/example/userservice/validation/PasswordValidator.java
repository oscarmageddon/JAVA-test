package com.example.userservice.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=(?:.*\\d){2})(?=(?:.*[A-Z]){1})[a-zA-Z\\d]{8,12}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) return false;
        
        // Check length
        if (password.length() < 8 || password.length() > 12) return false;
        
        // Count uppercase letters
        long uppercaseCount = password.chars()
            .filter(Character::isUpperCase)
            .count();
        
        // Count digits
        long digitCount = password.chars()
            .filter(Character::isDigit)
            .count();
        
        // Check if it has exactly one uppercase and exactly two digits
        return uppercaseCount == 1 && digitCount == 2;
    }
}