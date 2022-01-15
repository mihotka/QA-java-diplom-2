package com.yandex;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GetUserOrdersTest {

    UserData userData = UserData.getRandom();
    Login login = new Login();
    LoginData loginData = new LoginData(userData.email, userData.password);
    Registration registration = new Registration();
    DeleteUser deleteUser = new DeleteUser();
    UserOrders userOrders = new UserOrders();
    CreateOrder createOrder = new CreateOrder();

    @Test
    @DisplayName("Получение всех заказов пользователя с авторизацией")
    public void getAllUserOrdersWithAuth() {
        registration.create(userData);
        login.login(loginData);
        createOrder.createOrder("61c0c5a71d1f82001bdaaa6f");
        String orderNumber = createOrder.response.body().path("order.number").toString();
        userOrders.getAllUserOrders();
        userOrders.response.then().assertThat().statusCode(200);
        String userOrderNumber = userOrders.response.body().path("order.number").toString();
        assertEquals(orderNumber, userOrderNumber);
    }

    @Test
    @DisplayName("Получение всех заказов пользователя без авторизации")
    public void getAllUserOrdersWithNoAuth() {
        registration.create(userData);
        login.login(loginData);
        createOrder.createOrder("61c0c5a71d1f82001bdaaa6f");
        String orderNumber = createOrder.response.body().path("order.number").toString();
        userOrders.getAllUserOrders();
        userOrders.response.then().assertThat().statusCode(400);
    }

    @After
    public void delete() {
        deleteUser.delete(registration.response.path("accessToken").toString().substring(7));
    }
}
