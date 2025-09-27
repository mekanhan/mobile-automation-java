@calculator @demo @android
Feature: Android Calculator App Automation Demo
  As a QA engineer demonstrating mobile automation skills
  I want to automate basic calculator operations
  So that I can showcase functional UI testing capabilities

  Background:
    Given I launch the Android Calculator app
    And the calculator is ready for input

  @smoke @basic
  Scenario: Basic addition operation
    When I perform addition of 2 plus 3
    Then the result should be 5
    And the calculator display shows "5"

  @smoke @basic
  Scenario: Larger number addition
    When I perform addition of 15 plus 27
    Then the result should be 42
    And the calculator display shows "42"

  @functional @operations
  Scenario: Multiple consecutive operations
    When I enter number 10
    And I click add button
    And I enter number 5
    And I click equals button
    Then the result should be 15
    When I click add button
    And I enter number 3
    And I click equals button
    Then the result should be 18

  @functional @clear
  Scenario: Clear function resets calculator
    Given I enter number 123
    And I click add button
    And I enter number 456
    When I click clear button
    And I perform addition of 1 plus 1
    Then the result should be 2
    And the calculator display shows "2"

  @negative @validation
  Scenario: Calculator handles single digit operations
    When I click digit 7
    And I click add button
    And I click digit 3
    And I click equals button
    Then the result should be 10
    And the calculator display shows "10"

  @functional @subtraction
  Scenario: Basic subtraction operation
    When I enter number 10
    And I click subtract button
    And I enter number 4
    And I click equals button
    Then the result should be 6
    And the calculator display shows "6"

  @demo @showcase
  Scenario Outline: Multiple arithmetic operations showcase
    When I perform <operation> of <first_number> and <second_number>
    Then the result should be <expected_result>

    Examples:
      | operation    | first_number | second_number | expected_result |
      | addition     | 5            | 3             | 8               |
      | addition     | 12           | 8             | 20              |
      | subtraction  | 15           | 9             | 6               |
      | subtraction  | 20           | 5             | 15              |