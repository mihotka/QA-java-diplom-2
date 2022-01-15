package com.yandex;

import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Test;

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
    }

    @Test
    @Description("не логинитсья с невалидным мейлом")
    public void loginWithInvalidMail() {
        UserData userData = UserData.getRandom();
        LoginData loginData = new LoginData("XXXXtent@ya.otg", userData.password);
        registration.create(userData);
        login.login(loginData);
        login.response.then().assertThat().statusCode(401);
    }

    @Test
    @Description("не логинитсья с невалидным паролем")
    public void loginWithInvalidPassword() {
        UserData userData = UserData.getRandom();
        LoginData loginData = new LoginData(userData.email, "1234Bb1234");
        registration.create(userData);
        login.login(loginData);
        login.response.then().assertThat().statusCode(401);
    }

    @After
    public void delete () {
        deleteUser.delete(registration.response.path("accessToken").toString().substring(7));
    }
}
