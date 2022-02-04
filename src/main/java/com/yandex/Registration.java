package com.yandex;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class Registration extends RestAssuredClient {
    private static final String REGISTRATION_PATH = "api/auth/register";

    Response response;

    @Step
    public Response create(UserData userData) {
        return response = given()
                .spec(getBaseSpec())
                .body(userData)
                .when()
                .post(REGISTRATION_PATH);
    }
}
