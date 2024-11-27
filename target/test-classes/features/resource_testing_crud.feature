@active
@test1
Feature: Resource CRUD
  Scenario: Get active resources and update them to inactive
    Given there are at least 5 active resources in the system
    When I get the list of all my active resources
    Then the response status code is 200
    And the response body matches the expected schema
    When I update all of my active resources to inactive
    Then I should see all my resources as inactive

