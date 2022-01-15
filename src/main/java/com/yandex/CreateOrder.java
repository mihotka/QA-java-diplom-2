package com.yandex;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CreateOrder extends RestAssuredClient {
    private static final String ORDER_PATH = "api/orders";

    Response response;

    @Step
    public Response createOrder(String ingredientName) {
        String json = "{\"ingredients\": [\"" + ingredientName + "\" ]}";
        return response = given()
                .spec(getBaseSpec())
                .body(json)
                .when()
                .post(ORDER_PATH);
    }
}
