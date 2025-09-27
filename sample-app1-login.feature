Feature: Mobile App 1 - User Authentication
  As a user of Mobile App 1
  I want to authenticate securely
  So that I can access my personalized content

  Background:
    Given I have launched "Mobile App 1"
    And I am on the login screen

  @smoke @login @iosMobileApp1 @androidMobileApp1
  Scenario: Successful login with valid credentials
    When I enter "app1.user@example.com" as the username
    And I enter "SecurePass123!" as the password
    And I tap the "Login" button
    Then I should be redirected to the home dashboard
    And I should see "Welcome to Mobile App 1" message
    And the main navigation should be visible

  @regression @iosMobileApp1 @androidMobileApp1 @validation
  Scenario: Login validation displays appropriate errors
    When I leave the username field empty
    And I enter "ValidPass123!" as the password
    And I tap the "Login" button
    Then I should see the error "Username is required"
    And I should remain on the login screen

  @regression @iosMobileApp1 @androidMobileApp1 @security
  Scenario Outline: Login fails with invalid credentials for App 1
    When I enter "<username>" as the username
    And I enter "<password>" as the password
    And I tap the "Login" button
    Then I should see the error message "<error_message>"
    And I should remain on the login screen
    And the login attempt should be logged

    Examples:
      | username                    | password       | error_message                                   |
      | app1.user@example.com      | WrongPass123!  | Invalid username or password                   |
      | unknown.user@example.com   | SomePass123!   | User account not found                         |
      | app1.user@example.com      | weak           | Password does not meet security requirements   |
      | invalid.email.format       | ValidPass123!  | Please enter a valid email address            |

  @smoke @mobileApp1 @biometric @ios
  Scenario: Login with Touch ID for Mobile App 1 (iOS)
    Given Touch ID is enabled for Mobile App 1
    And I have previously authenticated successfully
    When I open Mobile App 1
    And I tap "Use Touch ID"
    And I authenticate with my fingerprint
    Then I should be automatically logged in
    And I should see the App 1 dashboard

  @smoke @mobileApp1 @biometric @android
  Scenario: Login with fingerprint for Mobile App 1 (Android)
    Given fingerprint authentication is enabled for Mobile App 1
    And I have previously authenticated successfully
    When I open Mobile App 1
    And I tap "Use Fingerprint"
    And I provide my fingerprint
    Then I should be automatically logged in
    And I should see the App 1 dashboard

  @regression @mobileApp1 @recovery
  Scenario: Password recovery for Mobile App 1 users
    When I tap the "Forgot Password?" link
    Then I should see the Mobile App 1 password recovery screen
    When I enter "app1.user@example.com" in the recovery email field
    And I tap "Send Reset Instructions"
    Then I should see "Password reset instructions have been sent to your email"
    And I should see a "Return to Login" button

  @regression @mobileApp1 @social @ios
  Scenario: Social login with Apple ID for Mobile App 1
    When I tap the "Continue with Apple" button
    And I complete the Apple ID authentication
    Then I should be logged in to Mobile App 1
    And I should see the App 1 personalized dashboard

  @regression @mobileApp1 @social @android
  Scenario: Social login with Google for Mobile App 1
    When I tap the "Sign in with Google" button
    And I complete the Google authentication flow
    Then I should be logged in to Mobile App 1
    And I should see the App 1 personalized dashboard

  @regression @mobileApp1 @session
  Scenario: Session persistence for Mobile App 1
    Given I have logged in to Mobile App 1 successfully
    When I enable "Keep me logged in"
    And I close and reopen Mobile App 1
    Then I should still be authenticated
    And I should see the App 1 dashboard immediately

  @regression @mobileApp1 @security @timeout
  Scenario: Session timeout handling in Mobile App 1
    Given I am logged into Mobile App 1
    When I leave the app idle for the configured timeout period
    And I try to access a protected feature
    Then I should see "Your session has expired for security"
    And I should be redirected to the Mobile App 1 login screen

  @regression @mobileApp1 @accessibility
  Scenario: Mobile App 1 login screen accessibility
    Then the Mobile App 1 login screen should have proper accessibility labels
    And the username field should be labeled "Email Address for Mobile App 1"
    And the password field should be labeled "Password for Mobile App 1"
    And the login button should be labeled "Sign In to Mobile App 1"
    And all interactive elements should support screen reader navigation