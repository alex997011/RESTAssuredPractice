package com.globant.api.requests;

import com.globant.api.utils.Constants;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class BaseRequest {

    /**
     * Executes a GET request to specified endpoint
     *
     * @param endpoint API endpoint URL
     * @param headers Request headers map
     * @return Response from the GET request
     */

    protected Response requestGet(String endpoint, Map<String, ?> headers) {
        return RestAssured.given()
                .contentType(Constants.VALUE_CONTENT_TYPE)
                .headers(headers)
                .when()
                .get(endpoint);
    }

    /**
     * Executes a POST request to specified endpoint
     *
     * @param endpoint API endpoint URL
     * @param headers Request headers map
     * @param body Request body object
     * @return Response from the POST request
     */

    protected Response requestPost(String endpoint, Map<String, ?> headers, Object body) {
        return RestAssured.given()
                .contentType(Constants.VALUE_CONTENT_TYPE)
                .headers(headers)
                .body(body)
                .when()
                .post(endpoint);
    }

    /**
     * Executes a PUT request to specified endpoint
     *
     * @param endpoint API endpoint URL
     * @param headers Request headers map
     * @param body Request body object
     * @return Response from the PUT request
     */

    protected Response requestPut(String endpoint, Map<String, ?> headers, Object body) {
        return RestAssured.given()
                .contentType(Constants.VALUE_CONTENT_TYPE)
                .headers(headers)
                .body(body)
                .when()
                .put(endpoint);
    }

    /**
     * Executes a DELETE request to specified endpoint
     *
     * @param endpoint API endpoint URL
     * @param headers Request headers map
     * @return Response from the DELETE request
     */

    protected Response requestDelete(String endpoint, Map<String, ?> headers) {
        return RestAssured.given()
                .contentType(Constants.VALUE_CONTENT_TYPE)
                .headers(headers)
                .when()
                .delete(endpoint);
    }

    /**
     * Creates a map of base HTTP headers
     *
     * @return Map containing content-type header
     */

    protected Map<String, String> createBaseHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.CONTENT_TYPE, Constants.VALUE_CONTENT_TYPE);
        return headers;
    }
}
