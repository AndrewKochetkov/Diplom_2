package ru.praktikum.user;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import ru.praktikum.data.User;

import static io.restassured.RestAssured.given;
import static ru.praktikum.data.Const.BASE_URI;
import static ru.praktikum.data.Const.CREATE_USER_PATH;

public class UserCreate {

    public UserCreate() {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Отправка запроса на создание пользователя")
    public Response createUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(CREATE_USER_PATH);
    }

    @Step("Отправка запроса на создание пользователя без пароля")
    public Response createUserWithoutPassoword(User user) {
        user.setPassword(null);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(CREATE_USER_PATH);
    }

    @Step("Отправка запроса на создание пользователя без email")
    public Response createUserWithoutEmail(User user) {
        user.setEmail(null);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(CREATE_USER_PATH);
    }

    @Step("Отправка запроса на создание пользователя без имени")
    public Response createUserWithoutName(User user) {
        user.setName(null);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(CREATE_USER_PATH);
    }
}