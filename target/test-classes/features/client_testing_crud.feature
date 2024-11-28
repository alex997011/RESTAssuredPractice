@active
  Feature: Client Testing CRUD
    Scenario: View all the clients
      Given there are registered clients in the system
      When I send a GET request to view all the clients
      Then the response validates the client information





    Scenario: Successfully update Laura's phone number
      Given the system has at least 10 registered clients
      And there is at least one client named "Laura" in the system
      And I retrieve the details of the first client named "Laura"
      And I store her current phone number
      When I send a PUT request to update the client with a new phone number
      Then the response should have a status code of 200
      And the new phone number should be different from the stored number
      And the response body should match the client JSON schema
      Then I delete all registered clients


      Scenario: Update and delete a New Client
        Given the system has at least 10 registered clients
        When I send a POST request to create a new client
        Then I find the new client in my list
        When I send a PUT request to update any parameter of the new client
        Then the response should have a status code of 200
        And the response body should match the new client JSON schema
        And the response body data should match the updated values
        Then I delete the new client



