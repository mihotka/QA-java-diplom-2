package com.yandex;

import io.qameta.allure.Step;

import static io.restassured.RestAssured.given;

public class DeleteUser extends RestAssuredClient {
    @Step
    public void delete(String token) {

        given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .when()
                .delete("api/auth/user")
                .then()
                .statusCode(202);
    }
}
