package com.yandex;

import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LoginTest {
    Registration registration = new Registration();
    Login login = new Login();
    DeleteUser deleteUser = new DeleteUser();

    @Test
    @Description("Успешная авторизация")
    public void loginByUser() {
        UserData userData = UserData.getRandom();
        LoginData loginData = new LoginData(userData.email, userData.password);
        registration.create(userData);
        login.login(loginData);
        login.response.then().assertThat().statusCode(200);
        assertEquals("Не совпадает success/fail в теле ответа", true, login.response.path("success"));
        assertEquals("В теле ответа отсутствует поле мейла", userData.email.toLowerCase(Locale.ROOT), login.response.path("user.email"));
        assertEquals("В теле ответа отсутствует поле имени", userData.name, login.response.path("user.name"));
        assertNotNull("Отсутствует accessToken", login.response.path("accessToken"));
    }

    @Test
    @Description("не логинитсья с невалидным мейлом")
    public void loginWithInvalidMail() {
        UserData userData = UserData.getRandom();
        LoginData loginData = new LoginData("XXXXtent@ya.otg", userData.password);
        registration.create(userData);
        login.login(loginData);
        login.response.then().assertThat().statusCode(401);
        assertEquals("Не совпадает сообщение об ошибке", "email or password are incorrect", login.response.path("message"));
    }

    @Test
    @Description("не логинитсья с невалидным паролем")
    public void loginWithInvalidPassword() {
        UserData userData = UserData.getRandom();
        LoginData loginData = new LoginData(userData.email, "1234Bb1234");
        registration.create(userData);
        login.login(loginData);
        login.response.then().assertThat().statusCode(401);
        assertEquals("Не совпадает сообщение об ошибке", "email or password are incorrect", login.response.path("message"));
    }

    @After
    public void delete() {
        deleteUser.delete(registration.response.path("accessToken").toString().substring(7));
    }
}
