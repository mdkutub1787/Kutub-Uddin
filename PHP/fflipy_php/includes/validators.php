<?php
/**
 * Validators - Mirroring lib/core/utils/validators.dart
 */
class Validators {
    public static function validateEmail($value) {
        if (empty($value)) return "The email field is required.";
        if (!filter_var($value, FILTER_VALIDATE_EMAIL)) return "Invalid email address";
        return null;
    }

    public static function validateUsername($value) {
        if (empty($value)) return "Username is required";
        if (strlen($value) < 5) return "The username must be at least 5 characters";
        return null;
    }

    public static function validatePassword($value) {
        if (empty($value)) return "Password is required";
        if (strlen($value) < 8) return "Password must be at least 8 characters";
        // Simple regex to check for uppercase, lowercase, number, and special char
        if (!preg_match('/[A-Z]/', $value) || !preg_match('/[a-z]/', $value) || !preg_match('/[0-9]/', $value) || !preg_match('/[^A-Za-z0-9]/', $value)) {
            return "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character";
        }
        return null;
    }

    public static function validateConfirmPassword($value, $password) {
        if (empty($value)) return "Confirm Password is required";
        if ($value !== $password) return "Passwords do not match";
        return null;
    }
}
?>
