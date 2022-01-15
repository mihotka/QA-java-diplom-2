package com.yandex;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserOrders extends RestAssuredClient{

    private static final String USERORDERS_PATH = "api/orders";

    Response response;

    public Response getAllUserOrders() {
        return response = given()
                .spec(getBaseSpec())
                .when()
                .post(USERORDERS_PATH);
    }
}
