@feed @crossPlatform @apiSetup
Feature: Social Feed
  As a logged-in user
  I want to view and interact with the social feed
  So that I can stay updated with announcements and posts

  Background:
    Given I am on the login page
    And I have valid user credentials
    When I login with valid credentials
    Then I should be successfully logged in
    And I navigate to Feed tab
    And I should see the Feed page

  @smoke @ios @android @announcements
  Scenario: View feed content
    Given I can see announcements in the feed
    Then I should see the feed content
    And I should see at least 1 announcements

  @functional @ios @android
  Scenario: Refresh feed content
    Given the feed is not empty
    When I pull to refresh the feed
    Then I should see updated feed content

  @functional @ios @android
  Scenario: Access compose feature
    When I tap the compose button
    Then I should see the compose interface

  @functional @ios @android
  Scenario: Filter feed content
    When I tap the filter button
    Then I should see the filter options

  @functional @ios @android @announcements
  Scenario: View announcement details
    Given I can see announcements in the feed
    When I tap the first announcement
    Then I should see the announcement details

  @integration @ios @android @announcements
  Scenario: Verify API-created announcement appears in feed
    Given I can see announcements in the feed
    Then I should see at least 1 announcements
    When I pull to refresh the feed
    Then I should see updated feed content

  @negative @ios @android
  Scenario: Handle empty feed state
    Given the feed is empty
    Then I should not see any posts
    And I should not see any announcements

  @functional @ios @android @announcements
  Scenario: Search for specific announcement
    Given I can see announcements in the feed
    When I search for post "Announcement body"
    Then I should see post "Announcement body"