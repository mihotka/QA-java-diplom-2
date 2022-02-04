package com.yandex;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GetUserData extends RestAssuredClient {

    private static final String UPD_USERDATA_PATH = "api/auth/user";

    Response response;

    @Step
    public Response getUserData(String token) {
        return response = given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .get(UPD_USERDATA_PATH);
    }
}

