package com.example.userservice.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {

    private PasswordValidator passwordValidator;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
    }

    @Test
    void isValid_ValidPassword_ReturnsTrue() {
        // Given
        String validPassword = "a2asfGfdfdf4";

        // When
        boolean result = passwordValidator.isValid(validPassword, null);

        // Then
        assertTrue(result);
    }

    @Test
    void isValid_ValidPasswordMinLength_ReturnsTrue() {
        // Given
        String validPassword = "aB2defg4";

        // When
        boolean result = passwordValidator.isValid(validPassword, null);

        // Then
        assertTrue(result);
    }

    @Test
    void isValid_ValidPasswordMaxLength_ReturnsTrue() {
        // Given
        String validPassword = "aB2defghijk4";

        // When
        boolean result = passwordValidator.isValid(validPassword, null);

        // Then
        assertTrue(result);
    }

    @Test
    void isValid_InvalidPasswordTooShort_ReturnsFalse() {
        // Given
        String invalidPassword = "aB2def4";

        // When
        boolean result = passwordValidator.isValid(invalidPassword, null);

        // Then
        assertFalse(result);
    }

    @Test
    void isValid_InvalidPasswordTooLong_ReturnsFalse() {
        // Given
        String invalidPassword = "aB2defghijklm4";

        // When
        boolean result = passwordValidator.isValid(invalidPassword, null);

        // Then
        assertFalse(result);
    }

    @Test
    void isValid_InvalidPasswordNoUppercase_ReturnsFalse() {
        // Given
        String invalidPassword = "a2asdfdfdf4";

        // When
        boolean result = passwordValidator.isValid(invalidPassword, null);

        // Then
        assertFalse(result);
    }

    @Test
    void isValid_InvalidPasswordTwoUppercase_ReturnsFalse() {
        // Given
        String invalidPassword = "a2asfGFdfdf4";

        // When
        boolean result = passwordValidator.isValid(invalidPassword, null);

        // Then
        assertFalse(result);
    }

    @Test
    void isValid_InvalidPasswordOneDigit_ReturnsFalse() {
        // Given
        String invalidPassword = "a2asfGdfdfdf";

        // When
        boolean result = passwordValidator.isValid(invalidPassword, null);

        // Then
        assertFalse(result);
    }

    @Test
    void isValid_InvalidPasswordThreeDigits_ReturnsFalse() {
        // Given
        String invalidPassword = "a2asfGdfdf24";

        // When
        boolean result = passwordValidator.isValid(invalidPassword, null);

        // Then
        assertFalse(result);
    }

    @Test
    void isValid_NullPassword_ReturnsFalse() {
        // Given
        String nullPassword = null;

        // When
        boolean result = passwordValidator.isValid(nullPassword, null);

        // Then
        assertFalse(result);
    }
}