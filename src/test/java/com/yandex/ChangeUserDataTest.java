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
        assertEquals("Измененный мейл и мейл из тела ответа не совпадают", userDataWithNoName.email, getUserData.response.body().path("user.email"));
        assertEquals("", userDataWithNoName.name, getUserData.response.body().path("user.name"));
    }

    @Test
    @DisplayName("Неуспешная смена данных пользователя без регистрации")
    public void failedChangeUserDataTestWithNoAuth() {
        registration.create(userData);
        changeUserData.updateUserData("Token", userDataWithNoName);
        changeUserData.response.then().assertThat().statusCode(403);
        assertEquals("Не совпадает сообщение об ошибке", "You should be authorised", changeUserData.response.path("message"));
        assertEquals("Не совпадает success/fail в теле ответа", "failed", changeUserData.response.path("success"));
    }

    @Test
    @DisplayName("Неуспешная смена данных пользователя на те же самые данные")
    public void failedChangeUserDataTestWithSameEmail() {
        registration.create(userData);
        String name = registration.response.path("name");
        String email = registration.response.path("email");
        UserEmailAndName userEmailAndName = new UserEmailAndName(email, name);
        changeUserData.updateUserDataSameMail(registration.response.path("accessToken").toString().substring(7), userEmailAndName);
        changeUserData.response.then().assertThat().statusCode(403);
        assertEquals("Не совпадает сообщение об ошибке", "User with such email already exists", changeUserData.response.path("message"));
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
