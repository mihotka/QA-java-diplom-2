package com.yandex;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

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
        createOrder.response.path("message").equals("You should be authorised");
        createOrder.response.path("success").equals(false);
    }

    @Test
    @DisplayName("Ошибка при отправлении запроса без ингредиентов")
    public void CreateOrderWithNoIngredientTest() {
        registration.create(userData);
        createOrder.createOrderWithNoIngredients(registration.response.path("accessToken").toString().substring(7));
        createOrder.response.then().assertThat().statusCode(400);
        createOrder.response.path("message").equals("Ingredient ids must be provided");
        createOrder.response.path("success").equals(false);
    }

    @After
    public void delete() {
        deleteUser.delete(registration.response.path("accessToken").toString().substring(7));
    }
}

