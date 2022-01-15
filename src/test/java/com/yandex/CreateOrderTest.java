package com.yandex;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

public class CreateOrderTest {
    CreateOrder createOrder = new CreateOrder();

    @Test
    @DisplayName("не успешное создание заказа без авторизации")
    public void CreateOrderWithoutAuthTest() {
        createOrder.createOrder("61c0c5a71d1f82001bdaaa6f");
        createOrder.response.then().assertThat().statusCode(400);
    }
}
