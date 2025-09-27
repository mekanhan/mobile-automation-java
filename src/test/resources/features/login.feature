@login @crossPlatform
Feature: User Authentication
  As a user of the mobile application
  I want to be able to login securely
  So that I can access my account and app features

  Background:
    Given I am on the login page

  @smoke @ios @android
  Scenario: Successful login with valid credentials
    Given I have valid user credentials
    When I enter valid username and password
    And I tap the login button
    Then I should be successfully logged in
    And I should see the main dashboard

  @smoke @ios @android @staff
  Scenario: Successful staff login
    Given I have valid staff credentials
    When I enter staff username and password
    And I tap the login button
    Then I should be successfully logged in
    And I should see the main dashboard

  @negative @ios @android
  Scenario: Failed login with invalid credentials
    When I enter username "invalid@example.com" and password "wrongpassword"
    And I tap the login button
    Then I should see an error message
    And I should remain on the login page

  @negative @ios @android
  Scenario: Failed login with empty username
    When I enter username "" and password "validpassword"
    And I tap the login button
    Then I should see error message "Username is required"
    And I should remain on the login page

  @negative @ios @android
  Scenario: Failed login with empty password
    When I enter username "valid@example.com" and password ""
    And I tap the login button
    Then I should see error message "Password is required"
    And I should remain on the login page

  @functional @ios @android
  Scenario: Quick login flow
    Given I have valid user credentials
    When I login with valid credentials
    Then I should be successfully logged in

  @functional @ios @android
  Scenario: Forgot password functionality
    When I tap forgot password link
    Then I should see the password reset screen