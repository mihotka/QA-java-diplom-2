package com.yandex;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChangeUserDataTest {

    Registration registration = new Registration();
    UserDataWithNoName userDataWithNoName = UserDataWithNoName.getRandom();

    UserData userData = UserData.getRandom();
    ChangeUserData changeUserData = new ChangeUserData();
    DeleteUser deleteUser = new DeleteUser();
    Login login = new Login();
    LoginData loginData = new LoginData(userData.email, userData.password);
    GetUserData getUserData = new GetUserData();
    @Test
    @DisplayName("Успешное изменение данных юзера с авторизациоей")
    public void successfulChangeUserDataTest() {
        registration.create(userData);
        login.login(loginData);
        changeUserData.updateUserData(registration.response.path("accessToken").toString().substring(7), userDataWithNoName);
        changeUserData.response.then().assertThat().statusCode(200);
        getUserData.getUserData(registration.response.path("accessToken").toString().substring(7));
        assertEquals(getUserData.response.body().path("user.email"), userDataWithNoName.email);
        assertEquals(getUserData.response.body().path("user.name"), userDataWithNoName.name);
    }

    @Test
    @DisplayName("Неуспешная смена данных пользователя без регистрации")
    public void failedChangeUserDataTestWithNoAuth() {
        registration.create(userData);
        changeUserData.updateUserData(registration.response.path("accessToken").toString().substring(7), userDataWithNoName);
        changeUserData.response.then().assertThat().statusCode(401);
    }

    @Test
    @DisplayName("Неуспешная смена данных пользователя а те же самые данные")
    public void failedChangeUserDataTestWithSameEmail() {
        registration.create(userData);
        UserDataWithNoName userDataWithNoName_2 = new UserDataWithNoName(userData.email, userData.password);
        changeUserData.updateUserData(registration.response.path("accessToken").toString().substring(7), userDataWithNoName_2);
        changeUserData.response.then().assertThat().statusCode(403);
    }
    @After
    public void deleteUser() {
        if (registration.response.body().path("success").equals(false)) {
            return;
        }
        registration.response.then().assertThat().statusCode(200);
        deleteUser.delete(registration.response.path("accessToken").toString().substring(7));
    }
}
