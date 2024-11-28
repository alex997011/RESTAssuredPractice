@test_all
Feature: Client Testing CRUD


    Scenario Outline: Successfully update Laura's phone number
      Given the system has at least 10 registered clients
      And there is at least one client named <clientName> in the system
      And I retrieve the details of the first client named <clientName>
      And I store her current phone number
      When I send a PUT request to update the client with a new phone number
      Then the response should have a status code of <expectedStatusCode>
      And the new phone number should be different from the stored number
      And the response body should match the client JSON schema
      Then I delete all registered clients
      Examples:
        | clientName | expectedStatusCode |
        | "Laura"    | 200                |

    Scenario Outline: Update and delete a New Client
      Given I send a GET request for the connection with my system
      When I send a POST request to create a new client
      Then I find the new client in my list
      When I send a PUT request to update any parameter of the new client
      Then the response should have a status code of <expectedStatusCode>
      And the response body should match the new client JSON schema
      And the response body data should match the updated values
      Then I DELETE the new client
      Examples:
        | expectedStatusCode |
        | 200                |



