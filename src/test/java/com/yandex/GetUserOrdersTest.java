package com.yandex;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GetUserOrdersTest {

    UserData userData = UserData.getRandom();
    Registration registration = new Registration();
    DeleteUser deleteUser = new DeleteUser();
    UserOrders userOrders = new UserOrders();
    CreateOrder createOrder = new CreateOrder();

    @Test
    @DisplayName("Получение всех заказов пользователя с авторизацией")
    public void getAllUserOrdersWithAuth() {
        registration.create(UserData.getRandom());
        String token = registration.response.path("accessToken").toString().substring(7);
        createOrder.createOrder("61c0c5a71d1f82001bdaaa6f", token);
        createOrder.response.then().assertThat().statusCode(200);
        String orderNumber = createOrder.response.body().path("order.number").toString();
        userOrders.getAllUserOrders(token);
        userOrders.response.then().assertThat().statusCode(200);
        userOrders.response.path("success").equals(true);
        Assert.assertNotNull(userOrders.response.path("total"));
        Assert.assertNotNull(userOrders.response.path("totalToday"));
        Assert.assertNotNull(userOrders.response.path("_id"));
        Assert.assertNotNull(userOrders.response.path("status"));
        Assert.assertNotNull(userOrders.response.path("createdAt"));
        Assert.assertNotNull(userOrders.response.path("updatedAt"));
        String userOrderNumber = userOrders.response.path("order.number").toString();
        assertEquals(orderNumber, userOrderNumber);
        deleteUser.delete(token);
    }

    @Test
    @DisplayName("Получение всех заказов пользователя без авторизации")
    public void getAllUserOrdersWithNoAuth() {
        registration.create(userData);
        String token = registration.response.path("accessToken").toString().substring(7);
        createOrder.createOrder("61c0c5a71d1f82001bdaaa6f", registration.response.path("accessToken").toString().substring(7));
        userOrders.getAllUserOrdersWitnNOAuth();
        userOrders.response.then().assertThat().statusCode(401);
        assertEquals("You should be authorised", userOrders.response.path("message"));
        deleteUser.delete(token);
    }

}
