package com.globant.api.stepDefinitions;

import com.globant.api.models.Client;
import com.globant.api.models.Resource;
import com.globant.api.requests.ClientRequest;
import com.globant.api.requests.ResourceRequest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class ResourceSteps
{
    private static final Logger logger = LogManager.getLogger(ResourceSteps.class);
    private final ResourceRequest resourceRequest = new ResourceRequest();
    private Response response;
    private List<Resource> activeResources;


    @Given("there are at least 5 active resources in the system")
    public void there_are_at_least_5_active_resources_in_the_system() {
        response = resourceRequest.getResources();
        List<Object> resources = response.jsonPath().getList("$");

        long activeResources = response.jsonPath().getList("findAll { it.active == true }").size();

        Assert.assertTrue(
                String.format("At least 5 active appeals were expected but were found %d", activeResources),
                activeResources >= 5
        );

        logger.info("We found {} active resources out of a total of  {} resources", activeResources, resources.size());
    }

    @When("I get the list of all my active resources")
    public void i_get_the_list_of_al_my_active_resources() {
        response = resourceRequest.getResources();
        List<Resource> allResources = resourceRequest.getResourceEntity(response);

        activeResources = allResources.stream()
                .filter(Resource::getActive)
                .collect(Collectors.toList());

        logger.info("Details of the resources that are active: {}", activeResources);
    }

    @Then("the response status code is {int}")
    public void verify_status_code(int expectedStatusCode) {
        logger.info("Response status code is : {}", response.getStatusCode());
        Assert.assertEquals(expectedStatusCode, response.getStatusCode());
    }

    @And("the response body matches the expected schema")
    public void verify_response_matches_resource_schema() {
        response.then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/resourceListSchema.json"));
        logger.info("Response body matches the resource list JSON schema");
    }

    @When("I update all of my active resources to inactive")
    public void update_all_of_my_active_resources_to_inactive() {
        boolean asInactive = false;
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("active", asInactive);


        for (Resource resource : activeResources) {
            Response updateResponse = resourceRequest.updateResource(resource.getId(), updateData);
            Assert.assertEquals(200, updateResponse.getStatusCode());
        }

        logger.info("Updated {} resources from active to inactive", activeResources.size());

    }



    @Then("I should see all my resources as inactive")
    public void i_should_see_all_my_resources_as_inactive() {
        response = resourceRequest.getResources();
        List<Resource> allResources = resourceRequest.getResourceEntity(response);

        // Verificar que todos los recursos estén inactivos
        boolean allInactive = allResources.stream()
                .noneMatch(Resource::getActive);

        Assert.assertTrue("Se encontraron recursos que aún están activos", allInactive);

        logger.info("Verified all {} resources are inactive", allResources.size());
        logger.info("Details of all resources and their status: {}", allResources);
    }

}
