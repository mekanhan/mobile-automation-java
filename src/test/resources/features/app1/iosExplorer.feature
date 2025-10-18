@ios @app1 @explorer
Feature: Wikipedia Explorer Page

  Background:
    Given I am on the Explorer page

  @smoke @navigation1
  Scenario: Verify Explorer page elements are visible
    When I scroll to "Today Header"
    Then I should see "Today Header"
    And I should see "Featured Article"
    And I should see "Search Field"
    # And I should see "Tabs Button"
    # And I should see "Profile Button"

  @smoke @search
  Scenario: Search for an article
    When I tap on "Search Field"
    And I enter "Appium" into "Search Field"
    And I hide the keyboard
    Then "Search Field" should contain text "Appium"

  @navigation @tabs
  Scenario: Navigate to Places tab
    When I navigate to "Places" tab
    Then I should see "Places Tab"

  @navigation @tabs
  Scenario: Navigate to Saved tab
    When I navigate to "Saved" tab
    Then I should see "Saved Tab"

  @navigation @tabs
  Scenario: Navigate to History tab
    When I navigate to "History" tab
    Then I should see "History Tab"

  @navigation @tabs
  Scenario: Navigate to Search tab
    When I navigate to "Search" tab
    Then I should see "Search Tab"

  @article @featured
  Scenario: Verify featured article is displayed
    Then I should see the featured article
    And "Article Title" should be visible
    And "Article Subtitle" should be visible
    And "Article Image" should be visible

  @article @save
  Scenario: Save featured article for later
    When I save the featured article
    Then "Save For Later" should be visible

  @article @interaction
  Scenario: Open article overflow menu
    When I open the article menu
    Then "Overflow Button" should be visible

  @profile
  Scenario: Open user profile
    When I open my profile
    # Add profile page verification when implemented

  @tabs
  Scenario: Open tabs view
    When I open tabs
    # Add tabs view verification when implemented

  @scroll
  Scenario: Scroll to Top Read section
    When I scroll to "Top Read"
    Then "Top Read" should be visible

  @regression
  Scenario: Verify all tab bar elements
    Then "Explore Tab" should be visible
    And "Places Tab" should be visible
    And "Saved Tab" should be visible
    And "History Tab" should be visible
    And "Search Tab" should be visible

  @regression
  Scenario: Verify navigation bar elements
    Then "Wikipedia Logo" should be visible
    And "Tabs Button" should be visible
    And "Profile Button" should be visible
    And "Search Field" should be visible

  @search @e2e
  Scenario Outline: Search for multiple topics
    When I tap on "Search Field"
    And I enter "<search_term>" into "Search Field"
    And I hide the keyboard
    Then "Search Field" should contain text "<search_term>"

    Examples:
      | search_term      |
      | Appium           |
      | Selenium         |
      | Mobile Testing   |

  @swipe
  Scenario: Swipe up on Explorer page
    When I swipe up
    Then I should be on the Explorer page

  @smoke @atomic-steps
  Scenario: Use atomic steps to interact with elements
    When I tap "Search Field"
    And I type "Test Automation" into "Search Field"
    Then "Search Field" should contain text "Test Automation"
    When I hide the keyboard
    And I tap "Tabs Button"
