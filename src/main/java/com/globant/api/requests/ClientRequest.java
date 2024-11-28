package com.globant.api.requests;

import com.globant.api.utils.Constants;
import com.globant.api.models.Client;
import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.globant.api.utils.Constants.BASE_URL;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class ClientRequest extends BaseRequest
{

    private String endpoint;
    private  static final Logger logger = LogManager.getLogger(ClientRequest.class);

    /**
     Get Client list

     @return rest-assured response
     */
    public Response getClients() {
        endpoint = BASE_URL + Constants.CLIENTS_PATH;
        return requestGet(endpoint, createBaseHeaders());
    }

    /**
     * Creates a new client with the specified data
     *
     * @param updateData Map containing client data (name, email, etc.)
     * @return Response object with the API response
     */

    public Response createClient(Map<String, Object> updateData) {
        endpoint = Constants.CLIENTS_PATH ;

        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.CONTENT_TYPE, Constants.VALUE_CONTENT_TYPE);

        return requestPost(
                endpoint,
                headers,
                updateData
        );
    }

    /**
     * Updates an existing client with specified data
     *
     * @param clientId ID of the client to update
     * @param updateData Map containing updated client data
     * @return Response object with the API response
     */

    public Response updateClient(String clientId, Map<String, Object> updateData) {
        endpoint = Constants.CLIENTS_PATH + "/" + clientId;

        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.CONTENT_TYPE, Constants.VALUE_CONTENT_TYPE);

        return requestPut(
                endpoint,
                headers,
                updateData
        );
    }


    /**
     * Deletes a client by their ID
     *
     * @param clientId ID of the client to delete
     * @return Response object containing the deletion result
     */

    public Response deleteClients(String clientId) {
        String endpoint = Constants.CLIENTS_PATH + "/" + clientId;
        logger.info("DELETE Request URL: {}{}", BASE_URL, endpoint);

        return requestDelete(
                endpoint,
                new HashMap<>()
        );
    }

    /**
     * Converts API response to list of Client objects
     *
     * @param response API response containing client data
     * @return List of Client objects parsed from response
     * */

    public List<Client> getClientsEntity(@NotNull Response response) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getList("", Client.class);
    }





}
