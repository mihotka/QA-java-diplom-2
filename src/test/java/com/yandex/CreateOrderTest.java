package com.yandex;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreateOrderTest {

    CreateOrder createOrder = new CreateOrder();
    UserData userData = UserData.getRandom();
    Registration registration = new Registration();
    DeleteUser deleteUser = new DeleteUser();

    @Test
    @DisplayName("не успешное создание заказа без авторизации")
    public void CreateOrderWithoutAuthTest() {
        createOrder.createOrderWithNoAuth("61c0c5a71d1f82001bdaaa6f");
        createOrder.response.then().assertThat().statusCode(400);
        assertEquals("Не совпадает сообщение об ошибке", "You should be authorised", createOrder.response.path("message"));
        assertEquals("Не совпадает success/fail в теле ответа", false, createOrder.response.path("success"));
    }

    @Test
    @DisplayName("Ошибка при отправлении запроса без ингредиентов")
    public void CreateOrderWithNoIngredientTest() {
        registration.create(userData);
        createOrder.createOrderWithNoIngredients(registration.response.path("accessToken").toString().substring(7));
        createOrder.response.then().assertThat().statusCode(400);
        assertEquals("Не совпадает сообщение об ошибке", "Ingredient ids must be provided", createOrder.response.path("message"));
        assertEquals("Не совпадает success/fail в теле ответа", false, createOrder.response.path("success"));
        deleteUser.delete(registration.response.path("accessToken").toString().substring(7));
    }
}

