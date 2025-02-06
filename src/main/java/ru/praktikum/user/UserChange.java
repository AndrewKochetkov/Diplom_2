package ru.praktikum.user;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import ru.praktikum.data.User;
import ru.praktikum.data.UserCreds;
import ru.praktikum.data.UserInfo;

import static io.restassured.RestAssured.given;
import static ru.praktikum.data.Const.*;
import static ru.praktikum.data.Const.CHANGE_USER_PATH;

public class UserChange {
    private Response response;
    private String accessToken;
    private UserCreds userCreds;
    private UserInfo userInfo;
    private UserCreate userCreate = new UserCreate();

    public UserChange() {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Отправка запроса на изменение email пользователя с авторизацией")
    public Response changeEmailUserWithAuth (User user) {
        response = userCreate.createUser(user);
        userCreds = response.as(UserCreds.class);
        accessToken = userCreds.getAccessToken().replaceFirst("Bearer ", "");

        userInfo = userCreds.getUser();
        userInfo.setEmail("test" + userInfo.getEmail());

        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .and()
                .body(userInfo)
                .when()
                .patch(CHANGE_USER_PATH);
    }

    @Step("Отправка запроса на изменение имени пользователя с авторизацией")
    public Response changeNameUserWithAuth (User user) {
        response = userCreate.createUser(user);
        userCreds = response.as(UserCreds.class);
        accessToken = userCreds.getAccessToken().replaceFirst("Bearer ", "");

        userInfo = userCreds.getUser();
        userInfo.setName("test" + userInfo.getName());

        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .and()
                .body(userInfo)
                .when()
                .patch(CHANGE_USER_PATH);
    }

    @Step("Отправка запроса на изменение email пользователя без авторизации")
    public Response changeEmailUserWithoutAuth (User user) {
        response = userCreate.createUser(user);
        userCreds = response.as(UserCreds.class);

        userInfo = userCreds.getUser();
        userInfo.setName("test" + userInfo.getName());

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(userInfo)
                .when()
                .patch(CHANGE_USER_PATH);
    }

    @Step("Отправка запроса на изменение имени пользователя без авторизации")
    public Response changeNameUserWithoutAuth (User user) {
        response = userCreate.createUser(user);
        userCreds = response.as(UserCreds.class);

        userInfo = userCreds.getUser();
        userInfo.setName("test" + userInfo.getName());

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(userInfo)
                .when()
                .patch(CHANGE_USER_PATH);
    }

    @Step("Отправка запроса на изменение существующего email с авторизацией")
    public Response changeExistEmailWithAuth (User user, User userSecond, String email) {
        userCreate.createUser(userSecond);
        response = userCreate.createUser(user);

        userCreds = response.as(UserCreds.class);
        accessToken = userCreds.getAccessToken().replaceFirst("Bearer ", "");

        userInfo = userCreds.getUser();
        userInfo.setEmail(userSecond.getEmail());

        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .and()
                .body(userInfo)
                .when()
                .patch(CHANGE_USER_PATH);
    }
}