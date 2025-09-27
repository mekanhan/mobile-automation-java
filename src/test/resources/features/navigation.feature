@navigation @crossPlatform
Feature: App Navigation
  As a logged-in user
  I want to navigate between different sections of the app
  So that I can access various features and content

  Background:
    Given I am on the login page
    And I have valid user credentials
    When I login with valid credentials
    Then I should be successfully logged in
    And I am on the dashboard

  @smoke @ios @android
  Scenario: Navigate to Feed tab
    When I navigate to Feed tab
    Then I should see the Feed page

  @smoke @ios @android
  Scenario: Navigate to Chat tab
    When I navigate to Chat tab
    Then I should see the Chat page

  @smoke @ios @android
  Scenario: Navigate to Assignments tab
    When I navigate to Assignments tab
    Then I should see the Assignments page

  @smoke @ios @android
  Scenario: Navigate to More tab
    When I navigate to More tab
    Then I should see the More page

  @functional @ios @android
  Scenario: Access profile from dashboard
    When I tap the profile button
    Then I should see the profile screen

  @functional @ios @android
  Scenario: Access notifications from dashboard
    When I tap the notifications button
    Then I should see the notifications screen

  @functional @ios @android
  Scenario: Access search from dashboard
    When I tap the search button
    Then I should see the search interface

  @smoke @ios @android
  Scenario Outline: Tab navigation verification
    When I navigate to <tab> tab
    Then I should see the <page> page

    Examples:
      | tab         | page        |
      | Feed        | Feed        |
      | Chat        | Chat        |
      | Assignments | Assignments |
      | More        | More        |