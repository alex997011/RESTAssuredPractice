package com.globant.api.stepDefinitions;

import com.globant.api.models.Client;
import com.globant.api.requests.ClientRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import java.util.List;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


public class ClientSteps
{
    private static final Logger logger = LogManager.getLogger(ClientSteps.class);
    private final ClientRequest clientRequest = new ClientRequest();
    private Response response;
    private Client client;


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
        try {
            // Obtener la lista de clientes
            response = clientRequest.getClients();

            // Validar respuesta básica
            Assert.assertNotNull("Response should not be null", response);
            Assert.assertEquals("Status code should be 200", 200, response.statusCode());

            // Obtener la lista de clientes
            List<Map<String, Object>> clients = response.jsonPath().getList("$");
            Assert.assertNotNull("Clients list should not be null", clients);

            // Contar cuántos clientes se llaman Laura
            long lauraCount = clients.stream()
                    .filter(client -> clientName.equals(client.get("name")))
                    .count();

            // Validar que exista al menos una Laura
            Assert.assertTrue(
                    String.format("Expected at least one client named %s but found %d", clientName, lauraCount),
                    lauraCount > 0
            );

            // Logging informativo
            logger.info("Found {} clients named {} in the system", lauraCount, clientName);

            // Log detallado de los clientes encontrados (útil para debugging)
            clients.stream()
                    .filter(client -> clientName.equals(client.get("name")))
                    .forEach(client -> logger.debug("Found client: {}", client));

        } catch (AssertionError e) {
            logger.error("Assertion failed while searching for client {}: {}", clientName, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while searching for client {}: {}", clientName, e.getMessage(), e);
            throw new RuntimeException("Failed to validate client named " + clientName, e);
        }
    }


    @Given("the system has at least 10 registered clients")
    public void system_has_least_10_registered_clients() {
        try {
            response = clientRequest.getClients();
            List<Object> clients = response.jsonPath().getList("$");

            Assert.assertNotNull("Clients list should not be null", clients);

            // Validar que haya al menos 10 clientes
            int clientCount = clients.size();
            Assert.assertTrue(
                    String.format("Expected at least 10 clients but found %d", clientCount),
                    clientCount >= 10
            );
            // Logging informativo
            logger.info("Found {} clients in the system", clientCount);
            logger.debug("Response body: {}", response.jsonPath().prettyPrint());
        } catch (AssertionError e) {
            logger.error("Assertion failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while validating clients: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to validate clients in system", e);
        }
    }

    @Given("I retrieve the details of the first client named \"Laura\"")
    public void retrieve_the_details_of_first_client_named(String clientName) {
        try {
            response = clientRequest.getClients();

            // Obtener y verificar el primer cliente de la lista
            List<Map<String, Object>> clients = response.jsonPath().getList("$");
            Map<String, Object> firstClient = clients.get(0);

            // Verificar que el primer cliente sea Laura
            Assert.assertEquals(
                    String.format("First client should be named %s", clientName),
                    clientName,
                    firstClient.get("name")
            );

            logger.info("First client is named: {}", clientName);

        } catch (Exception e) {
            logger.error("Error verifying first client: {}", e.getMessage());
            throw e;
        }
    }

    @When("I send a GET request to view all the clients")
    public void i_send_a_get_request_to_view_all_the_clients() {
        List<Client> clientList = clientRequest.getClientsEntity(response);
        Assert.assertNotNull(clientList);
        Assert.assertFalse(clientList.isEmpty());
    }

    @Then("the response validates the client information")
    public void the_response_validates_the_client_information() {
        response.then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/clientListSchema.json"));
        logger.info("Successfully validated schema from Client List object");
    }
   }
