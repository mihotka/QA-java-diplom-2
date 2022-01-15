package com.yandex;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class Login extends RestAssuredClient {
    private static final String LOGIN_PATH = "api/auth/login";

    Response response;

    @Step
    public Response login(LoginData loginData) {

        return response = given()
                .spec(getBaseSpec())
                .body(loginData)
                .when()
                .post(LOGIN_PATH);
    }
}
