package com.yandex;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RegistrationSameUserTest {


    @Test
    @DisplayName("Проверка регистрации пользователя с уже зарегистрированным мейлом")
    public void failedRegistrationWithSameMail() {

        UserData userData = UserData.getRandom();
        Registration registration = new Registration();
        DeleteUser deleteUser = new DeleteUser();

        registration.create(userData);
        assertEquals(registration.response.body().path("success"), true);
        String token = registration.response.path("accessToken").toString().substring(7);

        Response response2 = registration.create(userData);
        response2.then().statusCode(403);
        assertEquals(response2.body().path("success"), false);
        response2.body().path("message").equals("User already exists");

        deleteUser.delete(token);
    }
}
