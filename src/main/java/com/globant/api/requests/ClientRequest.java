package com.globant.api.requests;

import com.globant.api.utils.Constants;
import com.globant.api.models.Client;
import com.google.gson.Gson;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;


import java.util.List;

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
        endpoint = Constants.BASE_URL + Constants.CLIENTS_PATH;
        // Esto formará: https://6743f860b4e2e04abea02a29.mockapi.io/Clients

        logger.info("Calling endpoint: " + endpoint);
        Response response = requestGet(endpoint, createBaseHeaders());
        logger.info("Status code: " + response.getStatusCode());
        logger.info("Response body: " + response.asString());

        return response;
    }


    public Response getClients(String clientName) {
        endpoint = Constants.BASE_URL + Constants.CLIENTS_PATH;
        Response response = requestGet(endpoint, createBaseHeaders());
        return response;

    }

   // public Response updateClientPhone(Client client, String clientPhone) {
    //    endpoint = String.format(Constants.URL_WITH_PARAM, Constants.CLIENTS_PATH, clientPhone);
    //    return requestPut(endpoint, createBaseHeaders(), client);
    //}



    public Client getClientEntity(@NotNull Response response) {
        return response.as(Client.class);
    }

    public List<Client> getClientsEntity(@NotNull Response response) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getList("", Client.class);
    }


    public Client getClientEntity(String clientJson) {
        Gson gson = new Gson();
        return gson.fromJson(clientJson, Client.class);
    }

    public boolean validateSchema(Response response, String schemaPath) {
        try {
            response.then().assertThat().body(matchesJsonSchemaInClasspath(schemaPath));
            return true;
        }
        catch(AssertionError e) {
            return false;
        }
    }


}
