package com.globant.api.requests;

import com.globant.api.models.Client;
import com.globant.api.models.Resource;
import com.globant.api.utils.Constants;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.globant.api.utils.Constants.BASE_URL;

public class ResourceRequest extends BaseRequest{
    private String endpoint;
    private  static final Logger logger = LogManager.getLogger(ResourceRequest.class);

    /**
     * Retrieves all resources from the API
     *
     * @return Response containing list of resources
     */

    public Response getResources() {
        endpoint = BASE_URL + Constants.RESOURCES_PATH;
        return requestGet(endpoint, createBaseHeaders());
    }

    /**
     * Converts API response into list of Resource objects
     *
     * @param response API response containing resource data
     * @return List of Resource objects
     */
    public List<Resource> getResourceEntity(@NotNull Response response) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getList("", Resource.class);
    }

    /**
     * Updates a resource with specified data
     *
     * @param resourceId ID of resource to update
     * @param updateData Map of fields to update
     * @return API response
     */

    public Response updateResource(String resourceId, Map<String, Object> updateData) {
        endpoint = Constants.RESOURCES_PATH + "/" + resourceId;

        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.CONTENT_TYPE, Constants.VALUE_CONTENT_TYPE);

        return requestPut(
                endpoint,
                headers,
                updateData
        );
    }
}

