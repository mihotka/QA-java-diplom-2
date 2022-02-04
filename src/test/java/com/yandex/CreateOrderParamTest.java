package com.yandex;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CreateOrderParamTest extends RestAssuredClient {

    private final int expected;
    private final String ingredientName;

    CreateOrder createOrder = new CreateOrder();
    UserData userData = UserData.getRandom();
    Registration registration = new Registration();
    DeleteUser deleteUser = new DeleteUser();

    public CreateOrderParamTest(String ingredientName, int expected) {
        this.ingredientName = ingredientName;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Object[][] CreateOrderParamTesData() {
        return new Object[][]{
                {"61c0c5a71d1f82001bdaaa6f", 200},
                {"", 500},
                {"sm", 500},
        };
    }

    @Test
    @DisplayName("Параметризироованный тест на создание заказа: валидный ингредиент, без ингредиента, неверный ингредиент")
    public void validCreateOrderWithAuthTest() {
        registration.create(userData);
        createOrder.createOrder(ingredientName, registration.response.path("accessToken").toString().substring(7));
        createOrder.response.then().assertThat().statusCode(expected);
    }

    @After
    public void deleteUser() {
        if (registration.response.body().path("success").equals(false)) {
            assertEquals("Не совпадает сообщение об ошибке","Internal Server Error.",createOrder.response.path("message"));
            return;
        }
        Assert.assertNotNull("В теле ответа отсутствует имя заказа",createOrder.response.path("name"));
        Assert.assertNotNull("В теле ответа отсутствует номер заказа",createOrder.response.path("order.number"));
        registration.response.then().assertThat().statusCode(200);
        deleteUser.delete(registration.response.path("accessToken").toString().substring(7));
    }
}
