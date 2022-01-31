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
        assertEquals("Не совпадает success/fail в теле ответа",true, userOrders.response.path("success"));
        Assert.assertNotNull("В ответе отсутствует общее количество заказов", userOrders.response.path("total"));
        Assert.assertNotNull("В ответе отсутствует общее количество заказов за день",userOrders.response.path("totalToday"));
        Assert.assertNotNull("В ответе отсутствует айди",userOrders.response.path("_id"));
        Assert.assertNotNull("В ответе отсутствует статус заказа",userOrders.response.path("status"));
        Assert.assertNotNull("В ответе отсутствует время создание заказа",userOrders.response.path("createdAt"));
        Assert.assertNotNull("В ответе отсутствует время обновления статуса заказа",userOrders.response.path("updatedAt"));
        String userOrderNumber = userOrders.response.path("order.number").toString();
        assertEquals("Номера заквзов не совпадают",orderNumber, userOrderNumber);
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
        assertEquals("Не совпадает сообщение об ошибке","You should be authorised", userOrders.response.path("message"));
        deleteUser.delete(token);
    }

}
