package ru.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.data.User;
import ru.praktikum.data.UserGenerator;
import ru.praktikum.order.CreateOrder;
import ru.praktikum.user.UserAction;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static ru.praktikum.data.Const.*;

public class CreateOrderTest {
    private User user;
    private UserAction userAction;
    private Response response;
    private CreateOrder createOrder;

    @Before
    public void setUp() {
        user = UserGenerator.getUser();
        userAction = new UserAction();
        createOrder = new CreateOrder();
    }

    @Test
    @DisplayName("Создание заказа с авторизацией пользователя и с ингредиентами")
    @Description("Тест проверяет создание заказа с авторизацией пользователя и добавлением ингредиентов в заказ")
    public void createOrderWithAuthWithIngredientTest(){
        response = createOrder.createOrderWithAuthWithIngredient(user);
        userAction.deleteUser(userAction.login(user));
        response.then()
                .assertThat().body("success", equalTo(true))
                .and().assertThat().body("order.owner.email", equalTo(user.getEmail()))
                .and().assertThat().body("order.ingredients", notNullValue())
                .and().statusCode(SC_OK);
    }

    @Test
    @DisplayName("Создание заказа без авторизации пользователя и с ингредиентами")
    @Description("Тест проверяет создание заказа без авторизации пользователя с добавлением ингредиентов в заказ")
    public void createOrderWithoutAuthWithIngredientTest(){
        response = createOrder.createOrderWithoutAuthWithIngredient();
        response.then()
                .assertThat().body("success", equalTo(true))
                .and().assertThat().body("order.number", notNullValue())
                .and().statusCode(SC_OK);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией пользователя и без ингредиентов")
    @Description("Тест проверяет создание заказа с авторизацией пользователя, но без ингредиентов в заказе")
    public void createOrderWithAuthWithoutIngredientTest(){
        response = createOrder.createOrderWithAuthWithoutIngredient(user);
        userAction.deleteUser(userAction.login(user));
        response.then()
                .assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo(CREATE_ORDER_WITHOUT_INGREDIENT_ERROR))
                .and().statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Создание заказа без авторизации пользователя и без ингредиентов")
    @Description("Тест проверяет создание заказа без авторизации пользователя и без ингредиентов в заказе")
    public void createOrderWithoutAuthWithoutIngredientTest(){
        response = createOrder.createOrderWithoutAuthWithoutIngredient();
        response.then()
                .assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo(CREATE_ORDER_WITHOUT_INGREDIENT_ERROR))
                .and().statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Создание заказа без авторизации пользователя и с неверными ингредиентами")
    @Description("Тест проверяет создание заказа без авторизации пользователя с некорректными (неверными) ингредиентами")
    public void createOrderWithoutAuthWithWrongIngredientTest(){
        response = createOrder.createOrderWithoutAuthWithWrongIngredient();
        response.then().statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией пользователя и с неверными ингредиентами")
    @Description("Тест проверяет создание заказа с авторизацией пользователя с некорректными (неверными) ингредиентами")
    public void createOrderWithAuthWithWrongIngredientTest(){
        response = createOrder.createOrderWithAuthWithWrongIngredient(user);
        userAction.deleteUser(userAction.login(user));
        response.then().statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}
