package ru.praktikum.order;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import ru.praktikum.data.User;
import ru.praktikum.data.UserCreds;
import ru.praktikum.user.UserAction;

import static io.restassured.RestAssured.given;
import static ru.praktikum.data.Const.*;

public class OrderList {
    private Response response;
    private String accessToken;
    private UserCreds userCreds;
    private UserAction userAction = new UserAction();
    private CreateOrder createOrder = new CreateOrder();

    public OrderList() {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Отправка запроса на получение списка заказов с авторизацией")
    public Response getOrderListWithAuth(User user) {
        createOrder.createOrderWithAuthWithIngredient(user);
        response = userAction.login(user);
        userCreds = response.as(UserCreds.class);
        accessToken = userCreds.getAccessToken().replaceFirst("Bearer ", "");
        return given()
                .auth().oauth2(accessToken)
                .get(ORDER_PATH);
    }

    @Step("Отправка запроса на получение списка заказов без авторизации")
    public Response getOrderListWithoutAuth() {
        return given()
                .auth().oauth2("")
                .get(ORDER_PATH);
    }
}