package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.data.User;
import ru.praktikum.data.UserGenerator;
import ru.praktikum.order.OrderList;
import ru.praktikum.user.UserAction;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static ru.praktikum.data.Const.USER_DONT_AUTH_ERROR;

public class OrderListTest {
    private User user;
    private UserAction userAction;
    private Response response;
    private OrderList orderList;

    @Before
    public void setUp() {
        user = UserGenerator.getUser();
        userAction = new UserAction();
        orderList = new OrderList();
    }

    @Test
    @DisplayName("Получение списка заказов с авторизацией")
    @Description("Тест проверяет, что авторизованный пользователь может получить список своих заказов")
    public void getOrderListWithAuthTest(){
        response = orderList.getOrderListWithAuth(user);
        userAction.deleteUser(userAction.login(user));
        response.then()
                .assertThat().body("success", equalTo(true))
                .and().assertThat().body("orders", notNullValue())
                .and().statusCode(SC_OK);
    }

    @Test
    @DisplayName("Получение списка заказов без авторизации")
    @Description("Тест проверяет, что неавторизованный пользователь не может получить список заказов")
    public void getOrderListWithoutAuthTest(){
        response = orderList.getOrderListWithoutAuth();
        response.then()
                .assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo(USER_DONT_AUTH_ERROR))
                .and().statusCode(SC_UNAUTHORIZED);
    }
}