package com.yandex;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;

import static io.restassured.RestAssured.given;

public class ChangeUserData extends RestAssuredClient {
    public static void main( String[] args ) {}
    private static final String UPD_USERDATA_PATH = "api/auth/user";

    Response response;

    @Step
    public Response updateUserData(String token, UserDataWithNoName userDataWithNoName) {
        return response = given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .body(userDataWithNoName)
                .when()
                .patch(UPD_USERDATA_PATH);
    }
    @Step
    public Response updateUserDataSameMail(String token, UserEmailAndName userEmailAndName) {
        return response = given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .body(userEmailAndName)
                .when()
                .patch(UPD_USERDATA_PATH);
    }
}
