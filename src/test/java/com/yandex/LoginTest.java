package com.yandex;

import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Test;

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
        login.response.path("success").equals(true);
        login.response.path("user.email").equals(userData.email);
        login.response.path("user.name").equals(userData.name);
        assertNotNull(login.response.path("accessToken"));
    }

    @Test
    @Description("не логинитсья с невалидным мейлом")
    public void loginWithInvalidMail() {
        UserData userData = UserData.getRandom();
        LoginData loginData = new LoginData("XXXXtent@ya.otg", userData.password);
        registration.create(userData);
        login.login(loginData);
        login.response.then().assertThat().statusCode(401);
        login.response.path("message").equals("email or password are incorrect");
    }

    @Test
    @Description("не логинитсья с невалидным паролем")
    public void loginWithInvalidPassword() {
        UserData userData = UserData.getRandom();
        LoginData loginData = new LoginData(userData.email, "1234Bb1234");
        registration.create(userData);
        login.login(loginData);
        login.response.then().assertThat().statusCode(401);
        login.response.path("message").equals("email or password are incorrect");
    }

    @After
    public void delete () {
        deleteUser.delete(registration.response.path("accessToken").toString().substring(7));
    }
}
