@active

Feature: Resource CRUD
  Scenario: Get active resources and update them to inactive
    Given there are at least 5 active resources in the system
    When I get the list of all my active resources
    Then the response status code is 200
    And the response body matches the expected schema
    When I update all of my active resources to inactive
    Then I should see all my resources as inactive


  @test1
  Scenario: Update the last created resource
    Given the system has at least 15 registered resources
    When I send a GET request to view all the resources
    Then I find the last resource on my list
    When I send a PUT request to update all the parameters of the last resource
    Then the response status code is 200
    And the response body matches the expected schema JSON
    And the response body data should match the updated values in resource





