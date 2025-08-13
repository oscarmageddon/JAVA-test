package com.example.userservice.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {

    private EmailValidator emailValidator;

    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidator();
    }

    @Test
    void isValid_ValidEmail_ReturnsTrue() {
        // Given
        String validEmail = "test@domain.cl";

        // When
        boolean result = emailValidator.isValid(validEmail, null);

        // Then
        assertTrue(result);
    }

    @Test
    void isValid_ValidEmailWithNumbers_ReturnsTrue() {
        // Given
        String validEmail = "test123@domain123.cl";

        // When
        boolean result = emailValidator.isValid(validEmail, null);

        // Then
        assertTrue(result);
    }

    @Test
    void isValid_InvalidEmailMissingAtSymbol_ReturnsFalse() {
        // Given
        String invalidEmail = "testdomain.cl";

        // When
        boolean result = emailValidator.isValid(invalidEmail, null);

        // Then
        assertFalse(result);
    }

    @Test
    void isValid_InvalidEmailMissingDot_ReturnsFalse() {
        // Given
        String invalidEmail = "test@domaincl";

        // When
        boolean result = emailValidator.isValid(invalidEmail, null);

        // Then
        assertFalse(result);
    }

    @Test
    void isValid_InvalidEmailWithSpecialChars_ReturnsFalse() {
        // Given
        String invalidEmail = "test@domain.cl!";

        // When
        boolean result = emailValidator.isValid(invalidEmail, null);

        // Then
        assertFalse(result);
    }

    @Test
    void isValid_NullEmail_ReturnsFalse() {
        // Given
        String nullEmail = null;

        // When
        boolean result = emailValidator.isValid(nullEmail, null);

        // Then
        assertFalse(result);
    }

    @Test
    void isValid_EmptyEmail_ReturnsFalse() {
        // Given
        String emptyEmail = "";

        // When
        boolean result = emailValidator.isValid(emptyEmail, null);

        // Then
        assertFalse(result);
    }
}