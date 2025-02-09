package ru.praktikum.order;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import ru.praktikum.data.IngredientAll;
import ru.praktikum.data.IngredientForCreateOrder;
import ru.praktikum.data.User;
import ru.praktikum.data.UserCreds;
import ru.praktikum.user.UserCreate;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static ru.praktikum.data.Const.*;

public class CreateOrder {
    private Response response;
    private IngredientAll ingredientAll;
    private ArrayList<String> ingredients = new ArrayList<>();
    private String accessToken;
    private UserCreds userCreds;
    private IngredientForCreateOrder ingredientForCreateOrder;
    private UserCreate userCreate = new UserCreate();

    public CreateOrder() {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Отправка запроса для получения ID ингредиента")
    public IngredientAll getIngredientID() {
        return given()
                .header("Content-type", "application/json")
                .get(GET_INGREDIENT_PATH)
                .body().as(IngredientAll.class);
    }

    @Step("Отправка запроса для создания заказа с авторизацией и с ингредиентами")
    public Response createOrderWithAuthWithIngredient(User user) {
        response = userCreate.createUser(user);
        userCreds = response.as(UserCreds.class);
        accessToken = userCreds.getAccessToken().replaceFirst("Bearer ", "");
        ingredientAll = getIngredientID();
        ingredients.add(ingredientAll.getData().get(0).getId());
        ingredients.add(ingredientAll.getData().get(1).getId());
        ingredientForCreateOrder = new IngredientForCreateOrder(ingredients.toArray(new String[]{}));
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .and()
                .body(ingredientForCreateOrder)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Отправка запроса для создания заказа без авторизации и с ингредиентами")
    public Response createOrderWithoutAuthWithIngredient() {
        ingredientAll = getIngredientID();
        ingredients.add(ingredientAll.getData().get(0).getId());
        ingredients.add(ingredientAll.getData().get(1).getId());
        ingredientForCreateOrder = new IngredientForCreateOrder(ingredients.toArray(new String[]{}));

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(ingredientForCreateOrder)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Отправка запроса для создания заказа с авторизацией и без ингредиентов")
    public Response createOrderWithAuthWithoutIngredient(User user) {
        response = userCreate.createUser(user);
        userCreds = response.as(UserCreds.class);
        accessToken = userCreds.getAccessToken().replaceFirst("Bearer ", "");
        ingredientForCreateOrder = new IngredientForCreateOrder();

        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .and()
                .body(ingredientForCreateOrder)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Отправка запроса для создания заказа без авторизации и без ингредиентов")
    public Response createOrderWithoutAuthWithoutIngredient() {
        ingredientForCreateOrder = new IngredientForCreateOrder();

        return given()
                .header("Content-type", "application/json")
                .auth().oauth2("")
                .and()
                .body(ingredientForCreateOrder)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Отправка запроса для создания заказа без авторизации и с неправильным ингредиентом")
    public Response createOrderWithoutAuthWithWrongIngredient() {

        ingredientAll = getIngredientID();
        ingredients.add(ingredientAll.getData().get(0).getId() + "test");
        ingredients.add(ingredientAll.getData().get(1).getId());
        ingredientForCreateOrder = new IngredientForCreateOrder(ingredients.toArray(new String[]{}));

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(ingredientForCreateOrder)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Отправка запроса для создания заказа с авторизацией и с неправильным ингредиентом")
    public Response createOrderWithAuthWithWrongIngredient(User user) {
        response = userCreate.createUser(user);
        userCreds = response.as(UserCreds.class);
        accessToken = userCreds.getAccessToken().replaceFirst("Bearer ", "");

        ingredientAll = getIngredientID();
        ingredients.add(ingredientAll.getData().get(0).getId() + "test");
        ingredients.add(ingredientAll.getData().get(1).getId());

        ingredientForCreateOrder = new IngredientForCreateOrder(ingredients.toArray(new String[]{}));

        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .and()
                .body(ingredientForCreateOrder)
                .when()
                .post(ORDER_PATH);
    }
}