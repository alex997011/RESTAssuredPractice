package com.globant.api.stepDefinitions;

import com.globant.api.models.Client;
import com.globant.api.requests.ClientRequest;
import com.globant.api.utils.Constants;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.*;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


public class ClientSteps {
    private static final Logger logger = LogManager.getLogger(ClientSteps.class);
    private final ClientRequest clientRequest = new ClientRequest();
    private Response response;
    private String storedPhoneNumber;
    private Map<String, Object> currentClient;
    private String updatedField;
    private String updatedValue;


    @Given("there are registered clients in the system")
    public void there_are_registered_clients_in_the_system() {
        try {
            response = clientRequest.getClients();
            if (response != null) {
                logger.info("Response: " + response.jsonPath().prettyPrint());
                Assert.assertEquals(200, response.statusCode());
            } else {
                logger.error("Response is null");
                throw new AssertionError("Response is null");
            }
        } catch (Exception e) {
            logger.error("Error in step: " + e.getMessage());
            throw e;
        }
    }

    @Given("there is at least one client named {string} in the system")
    public void there_is_least_one_client_with_name(String clientName) {
        response = clientRequest.getClients();
        List<Map<String, Object>> clients = response.jsonPath().getList("$");

        long clientsFound = clients.stream()
                .filter(client -> clientName.equals(client.get("name")))
                .count();

        Assert.assertTrue(
                String.format("Expected at least one client named %s but found %d", clientName, clientsFound),
                clientsFound > 0
        );

        logger.info("Found {} clients named {}", clientsFound, clientName);
    }


    @Given("the system has at least 10 registered clients")
    public void system_has_least_10_registered_clients() {
        response = clientRequest.getClients();
        List<Object> clients = response.jsonPath().getList("$");

        int clientCount = clients.size();
        Assert.assertTrue(
                String.format("Expected at least 10 clients but found %d", clientCount),
                clientCount >= 10
        );

        logger.info("Found at least {} clients in the system", clientCount);
    }

    @Given("I retrieve the details of the first client named {string}")
    public void retrieve_the_details_of_first_client_named(String clientName) {
        response = clientRequest.getClients();
        currentClient = response.jsonPath().getMap(String.format("find {it.name == '%s'}", clientName));
        Assert.assertEquals(clientName, currentClient.get("name"));
        logger.info("Details of the first client named Laura: {}", currentClient);
    }

    @And("I store her current phone number")
    public void store_current_phone_number() {
        storedPhoneNumber = currentClient.get("phone").toString();
        logger.info("Stored her current phone number: {}", storedPhoneNumber);
    }

    @When("I send a GET request to view all the clients")
    public void i_send_a_get_request_to_view_all_the_clients() {
        List<Client> clientList = clientRequest.getClientsEntity(response);
        Assert.assertNotNull(clientList);
        Assert.assertFalse(clientList.isEmpty());
    }

    @When("I send a PUT request to update the client with a new phone number")
    public void send_put_request_to_update_client_phone() {
        String newPhoneNumber = "999-" + System.currentTimeMillis();// creation of a random

        Map<String, Object> updateData = new HashMap<>();
        updateData.put("phone", newPhoneNumber);

        String clientId = currentClient.get("id").toString();
        response = clientRequest.updateClient(clientId, updateData);

        logger.info("Updating phone number for client ID: {} from {} to {}",
                clientId, storedPhoneNumber, newPhoneNumber);
    }

    @When("I send a POST request to create a new client")
    public void send_pot_request_to_create_new_client() {
        Map<String, Object> newClient = new HashMap<>();
        newClient.put("name", "Test Client");
        newClient.put("lastName", "Test LastName");
        newClient.put("country", "Test Country");
        newClient.put("city", "Test City");
        newClient.put("email", "test@test.com");
        newClient.put("phone", "1234567890");

        response = clientRequest.createClient(newClient);

    }

    @When("I send a PUT request to update any parameter of the new client")
    public void     i_send_a_put_request_to_update_parameter_of_new_client() {
        String[] parameters = {"name", "lastName", "country", "city", "email", "phone"};

        Random random = new Random();
        updatedField = parameters[random.nextInt(parameters.length)];
        updatedValue = "Updated " + updatedField;

        Map<String, Object> updateData = new HashMap<>();
        updateData.put(updatedField, updatedValue);

        String clientId = response.jsonPath().getString("id");
        response = clientRequest.updateClient(clientId, updateData);

        logger.info("Updated field '{}' with value '{}'", updatedField, updatedValue);
    }



    @Then("the response validates the client information")
    public void the_response_validates_the_client_information() {
        response.then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/clientListSchema.json"));
        logger.info("Successfully validated schema from Client List object");
    }

    @Then("the response should have a status code of {int}")
    public void verify_status_code(int expectedStatusCode) {
        logger.info("Response status code is : {}", response.getStatusCode());
        Assert.assertEquals(expectedStatusCode, response.getStatusCode());
    }


    @Then("the new phone number should be different from the stored number")
    public void verify_new_phone_number_is_different() {
        response = clientRequest.getClients();
        currentClient = response.jsonPath().getMap("find {it.name == 'Laura'}");

        String currentPhoneNumber = currentClient.get("phone").toString();

        Assert.assertNotEquals(
                "Phone number should have changed",
                storedPhoneNumber,
                currentPhoneNumber
        );
        logger.info("Phone number changed from {} to {}", storedPhoneNumber, currentPhoneNumber);
    }

    @Then("I delete all registered clients")
    public void delete_all_registered_clients() {
        logger.warn("Note: After deleting all clients, you'll need to regenerate test data in MockAPI");

        response = clientRequest.getClients();
        List<Map<String, Object>> clients = response.jsonPath().getList("$");

        for(Map<String, Object> client : clients) {
            String clientId = client.get("id").toString();
            logger.info("Attempting to delete client with ID: {}", clientId);

            Response deleteResponse = clientRequest.deleteClients(clientId);
            logger.info("Delete response status: {}", deleteResponse.getStatusCode());

            deleteResponse.getStatusCode();

            Assert.assertEquals(200, deleteResponse.getStatusCode());
        }

        logger.info("Successfully deleted all clients");
    }

    @Then("I find the new client in my list")
    public void i_find_the_new_client_in_my_list() {

        Response listResponse = clientRequest.getClients();
        List<Client> clients = listResponse.jsonPath().getList("$", Client.class);

        String newClientId = response.jsonPath().getString("id");

        Client foundClient = clients.stream()
                .filter(client -> client.getId().equals(newClientId))
                .findFirst()
                .orElse(null);

        Assert.assertNotNull("El nuevo cliente no se encontró en la lista", foundClient);

        logger.info("New client with ID {} was found in the list", newClientId);
        logger.info("New client details: {}", foundClient);
        }



    @And("the response body should match the client JSON schema")
    public void verify_response_matches_client_schema() {
        response.then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/clientListSchema.json"));
        logger.info("Response body matches the client list JSON schema");
    }


    @And("the response body should match the new client JSON schema")
    public void verify_response_matches_new_client_schema() {
        response.then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/clientSchema.json")); // Usando el schema correcto

        logger.info("Response body successfully validated against client schema");
    }

    @And("the response body data should match the updated values")
    public void verify_response_body_matches_updated_values() {
        JsonPath jsonResponse = response.jsonPath();

        Assert.assertEquals(updatedValue, jsonResponse.getString(updatedField));

        logger.info("Verified that field '{}' was updated to '{}'", updatedField, updatedValue);
    }

    @Then("I delete the new client")
    public void i_delete_the_new_client() {
        String clientId = response.jsonPath().getString("id");
        response = clientRequest.deleteClients(clientId);

        Assert.assertEquals(200, response.getStatusCode());

        logger.info("Deleted client successful with ID: {}", clientId);

    }
}
