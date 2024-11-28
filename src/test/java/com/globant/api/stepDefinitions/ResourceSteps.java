package com.globant.api.stepDefinitions;

import com.globant.api.models.Client;
import com.globant.api.models.Resource;
import com.globant.api.requests.ClientRequest;
import com.globant.api.requests.ResourceRequest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class ResourceSteps
{
    private static final Logger logger = LogManager.getLogger(ResourceSteps.class);
    private final ResourceRequest resourceRequest = new ResourceRequest();
    private Response response;
    private List<Resource> activeResources;
    private Resource lastResource;



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

    @Given("the system has at least 15 registered resources")
    public void there_are_at_least_15_registered_resources_in_the_system() {
        response = resourceRequest.getResources();
        List<Object> resources = response.jsonPath().getList("$");

        int resourceCount = resources.size();
        Assert.assertTrue(
                String.format("Expected at least 15 resources but found %d", resourceCount),
                resourceCount >= 15
        );

        logger.info("Found at least {} registered resources in the system", resourceCount);
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

    @When("I send a GET request to view all the resources")
    public void i_send_a_get_request_to_view_all_the_resources() {
        response = resourceRequest.getResources();
        List<Resource> resourceList = resourceRequest.getResourceEntity(response);
        Assert.assertNotNull(resourceList);
        Assert.assertFalse(resourceList.isEmpty());
        logger.info("Retrieved resources: {}", resourceList);
    }

    @When("I send a PUT request to update all the parameters of the last resource")
    public void i_send_a_put_request_to_update_all_parameters() {
        Map<String, Object> updateData = new HashMap<>();

        updateData.put("name", "Updated Name");
        updateData.put("trademark", "Updated Trademark");
        updateData.put("stock", 100);
        updateData.put("price", 99.99);
        updateData.put("description", "Updated Description");
        updateData.put("tags", "Updated Tags");

        boolean currentActiveStatus = lastResource.getActive();
        updateData.put("active", !currentActiveStatus);

        String resourceId = lastResource.getId();
        response = resourceRequest.updateResource(resourceId, updateData);

        logger.info("=== Updated all fields of Resource with ID: {} ===\n" +
                        "name: {}\n" +
                        "trademark: {}\n" +
                        "stock: {}\n" +
                        "price: {}\n" +
                        "description: {}\n" +
                        "tags: {}\n" +
                        "active: {} (changed from {})",
                resourceId,
                updateData.get("name"),
                updateData.get("trademark"),
                updateData.get("stock"),
                updateData.get("price"),
                updateData.get("description"),
                updateData.get("tags"),
                updateData.get("active"),
                currentActiveStatus
        );
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

    @And("the response body matches the expected schema JSON")
    public void verify_response_matches_resource_schema_json() {
        response.then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/resourceSchema.json"));
        logger.info("Response body matches the resource JSON schema");
    }


    @And("the response body data should match the updated values in resource")
    public void verify_response_body_matches_updated_values_in_resource() {
        JsonPath jsonResponse = response.jsonPath();

        Assert.assertEquals("Updated Name", jsonResponse.get("name"));
        Assert.assertEquals("Updated Trademark", jsonResponse.get("trademark"));
        Assert.assertEquals(Integer.valueOf(100), jsonResponse.get("stock"));
        Assert.assertEquals(Float.valueOf(99.99f), jsonResponse.get("price"));
        Assert.assertEquals("Updated Description", jsonResponse.get("description"));
        Assert.assertEquals("Updated Tags", jsonResponse.get("tags"));
        Assert.assertEquals(!lastResource.getActive(), jsonResponse.get("active"));


        logger.info("All resource values were successfully verified");
    }

    @Then("I should see all my resources as inactive")
    public void i_should_see_all_my_resources_as_inactive() {
        response = resourceRequest.getResources();
        List<Resource> allResources = resourceRequest.getResourceEntity(response);

        boolean allInactive = allResources.stream()
                .noneMatch(Resource::getActive);

        Assert.assertTrue("Se encontraron recursos que aún están activos", allInactive);

        logger.info("Verified all {} resources are inactive", allResources.size());
        logger.info("Details of all resources and their status: {}", allResources);
    }

    @Then("I find the last resource on my list")
    public void i_find_the_last_resource_on_my_list() {
        List<Resource> resourceList = resourceRequest.getResourceEntity(response);

        Assert.assertFalse("Resource list is empty", resourceList.isEmpty());

        lastResource = resourceList.get(resourceList.size() - 1);

        logger.info("=== Last Resource Found === ID: {}, Name: {}, Trademark: {}, Stock: {}, Price: {}. Description: {}, Tags: {}, Active: {}  ===",
                lastResource.getId(),
                lastResource.getName(),
                lastResource.getTrademark(),
                lastResource.getStock(),
                lastResource.getPrice(),
                lastResource.getDescription(),
                lastResource.getTags(),
                lastResource.getActive());
    }
}
