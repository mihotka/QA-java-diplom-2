package com.yandex;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserOrders extends RestAssuredClient{

    private static final String USERORDERS_PATH = "api/orders";

    Response response;

    public Response getAllUserOrdersWitnNOAuth() {
        return response = given()
                .spec(getBaseSpec())
                .when()
                .get(USERORDERS_PATH);
    }
    public Response getAllUserOrders(String token) {
        return response = given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .when()
                .get(USERORDERS_PATH);
    }
}
