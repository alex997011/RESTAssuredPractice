package com.globant.api.stepDefinitions;

import com.globant.api.models.Client;
import com.globant.api.requests.ClientRequest;
import com.globant.api.utils.Constants;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


public class ClientSteps
{
    private static final Logger logger = LogManager.getLogger(ClientSteps.class);
    private final ClientRequest clientRequest = new ClientRequest();
    private Response response;
    private String storedPhoneNumber;
    private Map<String, Object> currentClient;



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
        String newPhoneNumber = "999-" + System.currentTimeMillis();// creation of a random number phone

        Map<String, Object> updateData = new HashMap<>();
        updateData.put("phone", newPhoneNumber);

        String clientId = currentClient.get("id").toString();
        response = clientRequest.updateClient(clientId, updateData);

        logger.info("Updating phone number for client ID: {} from {} to {}",
                clientId, storedPhoneNumber, newPhoneNumber);
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
        // Obtener cliente actualizado
        response = clientRequest.getClients();
        currentClient = response.jsonPath().getMap("find {it.name == 'Laura'}");

        // Obtener número actual
        String currentPhoneNumber = currentClient.get("phone").toString();

        // Verificar que cambió
        Assert.assertNotEquals(
                "Phone number should have changed",
                storedPhoneNumber,  // número guardado anteriormente
                currentPhoneNumber  // número actual
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



    @And("the response body should match the client JSON schema")
    public void verify_response_matches_client_schema() {
        response.then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/clientListSchema.json"));
        logger.info("Response body matches the client list JSON schema");
    }

   }
