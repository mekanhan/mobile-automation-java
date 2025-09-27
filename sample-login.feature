Feature: Mobile Application Login
  As a mobile app user
  I want to be able to log into the application
  So that I can access my personalized content

  Background:
    Given the application is launched
    And I am on the login screen

  @smoke @login @ios @android
  Scenario: Successful login with valid credentials
    When I enter "demo.user@example.com" as the username
    And I enter "SecurePass123!" as the password
    And I tap on the "Login" button
    Then I should be redirected to the home screen
    And I should see the welcome message "Welcome back, Demo User!"
    And the navigation menu should be visible

  @regression @login @validation
  Scenario: Login validation for empty fields
    When I tap on the "Login" button without entering credentials
    Then I should see the error message "Email is required"
    And the login button should remain on the screen

  @regression @login @validation
  Scenario: Login fails with invalid email format
    When I enter "invalid.email" as the username
    And I enter "ValidPass123!" as the password
    And I tap on the "Login" button
    Then I should see the error message "Please enter a valid email address"

  @regression @login @security
  Scenario Outline: Login fails with invalid credentials
    When I enter "<username>" as the username
    And I enter "<password>" as the password
    And I tap on the "Login" button
    Then I should see the error message "<error_message>"
    And I should remain on the login screen

    Examples:
      | username                  | password       | error_message                                    |
      | valid.user@example.com    | WrongPass123!  | Invalid username or password                    |
      | unknown.user@example.com  | SomePass123!   | Invalid username or password                    |
      | demo.user@example.com     | shortpw        | Password must be at least 8 characters          |
      | demo.user@example.com     | nouppercasepw  | Password must contain uppercase and lowercase   |
      | demo.user@example.com     | NoNumbers!     | Password must contain at least one number       |

  @smoke @login @biometric @ios
  Scenario: Login with Face ID (iOS)
    Given Face ID is enabled on the device
    And I have previously logged in successfully
    When I open the application
    And I tap on "Use Face ID" button
    And I authenticate with Face ID
    Then I should be logged in automatically
    And I should see the home screen

  @smoke @login @biometric @android
  Scenario: Login with fingerprint (Android)
    Given fingerprint authentication is enabled on the device
    And I have previously logged in successfully
    When I open the application
    And I tap on "Use Fingerprint" button
    And I authenticate with my fingerprint
    Then I should be logged in automatically
    And I should see the home screen

  @regression @login @recovery
  Scenario: Password recovery flow
    When I tap on the "Forgot Password?" link
    Then I should see the password recovery screen
    When I enter "demo.user@example.com" as the recovery email
    And I tap on the "Send Reset Link" button
    Then I should see the message "Password reset instructions have been sent to your email"
    And I should see the "Return to Login" button

  @regression @login @social
  Scenario: Login with Google account
    When I tap on the "Continue with Google" button
    And I complete the Google authentication flow
    Then I should be logged in successfully
    And I should see the home screen

  @regression @login @persistence
  Scenario: Remember me functionality
    When I enter "demo.user@example.com" as the username
    And I enter "SecurePass123!" as the password
    And I enable the "Remember Me" checkbox
    And I tap on the "Login" button
    Then I should be logged in successfully
    When I close and reopen the application
    Then I should remain logged in
    And I should see the home screen

  @regression @login @security @timeout
  Scenario: Session timeout handling
    Given I am logged into the application
    When I leave the application idle for 15 minutes
    And I try to navigate to a protected screen
    Then I should see the message "Your session has expired"
    And I should be redirected to the login screen

  @regression @login @accessibility
  Scenario: Login screen accessibility
    Then the login screen should have proper accessibility labels
    And the username field should have the label "Email Address"
    And the password field should have the label "Password"
    And the login button should have the label "Sign In"
    And all interactive elements should be accessible via screen reader