package com.automation.restutils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;

import java.util.Map;

import static io.restassured.RestAssured.*;

public class RestUtils {
    public Response get(String url) {
        return RestAssured.given().
                header("authorization", "bearer").
                header("Content-Type", "application/json").log().all().
                get(url);
    }

    public  Response getWithParams(String url , Map<String, String> map)
    {
        RequestSpecification requestSpecification = given().
                baseUri("https://api.postman.com").
                header("X-Api-Key", "PMAK-623310aeb3148544e5c50cfb-81ebf9db0aa47d51ca5146639a76e27cf3");

        return   given(requestSpecification).

                //.param(key,id)
                queryParams(map)
                .log().all().get(url);
    }
    public  Response getId(String url , Map<String, Integer> map)
    {

        return   given().

                //.param(key,id)
                        queryParams(map)
                .log().all().get(url);
    }
}
