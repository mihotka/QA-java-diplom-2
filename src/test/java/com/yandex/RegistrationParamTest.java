package com.yandex;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.apache.commons.lang3.RandomStringUtils;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class RegistrationParamTest {
    private final String mail;
    private final String password;
    private final String name;
    private final boolean expected;
    Registration registration = new Registration();
    DeleteUser deleteUser = new DeleteUser();

    public RegistrationParamTest(String mail, String password, String name, boolean expected) {
        this.mail = mail;
        this.password = password;
        this.name = name;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Object[][] RegistrationParamTestData() {
        return new Object[][]{
                {null, "1234Bb", "Mike", false},
                {"st" + RandomStringUtils.random(5) + "@gmail.su", null, "Mike", false},
                {"sm" + RandomStringUtils.random(5) + "@gmail.su", "1234Bb", null, false},
                {"sv" + RandomStringUtils.random(5) + "@gmail.su", "1234Bb", "Mike", true},
        };
    }

    @Test
    @DisplayName("Проверка регистрации нового пользователя")
    public void failedRegistrationWithNullMail() {
        UserData userData = new UserData(mail, password, name);
        registration.create(userData);
        assertEquals(registration.response.body().path("success"), expected);
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

