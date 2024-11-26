package com.globant.api.requests;

import com.globant.api.utils.Constants;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class BaseRequest {

    protected Response requestGet(String endpoint, Map<String, ?> headers) {
        return RestAssured.given()
                .contentType(Constants.VALUE_CONTENT_TYPE)
                .headers(headers)
                .when()
                .get(endpoint);
    }

    protected Response requestPost(String endpoint, Map<String, ?> headers, Object body) {
        return RestAssured.given()
                .contentType(Constants.VALUE_CONTENT_TYPE)
                .headers(headers)
                .body(body)
                .when()
                .post(endpoint);
    }

    protected Response requestPut(String endpoint, Map<String, ?> headers, Object body) {
        return RestAssured.given()
                .contentType(Constants.VALUE_CONTENT_TYPE)
                .headers(headers)
                .body(body)
                .when()
                .put(endpoint);
    }

    protected Response requestDelete(String endpoint, Map<String, ?> headers) {
        return RestAssured.given()
                .contentType(Constants.VALUE_CONTENT_TYPE)
                .headers(headers)
                .when()
                .delete(endpoint);
    }

    protected Map<String, String> createBaseHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.CONTENT_TYPE, Constants.VALUE_CONTENT_TYPE);
        return headers;
    }
}
