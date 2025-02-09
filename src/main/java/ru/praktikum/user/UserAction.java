package ru.praktikum.user;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import ru.praktikum.data.TokenForAuth;
import ru.praktikum.data.User;
import ru.praktikum.data.UserCreds;

import static io.restassured.RestAssured.given;
import static ru.praktikum.data.Const.*;

public class UserAction {
    private String accessToken;
    private UserCreds userCreds;
    private TokenForAuth tokenForAuth;

    public UserAction() {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Отправка запроса на удаление пользователя")
    public Response deleteUser (Response response) {
        userCreds = response.as(UserCreds.class);
        accessToken = userCreds.getAccessToken().replaceFirst("Bearer ", "");
        return given()
                .auth().oauth2(accessToken)
                .when()
                .delete(CHANGE_USER_PATH);
    }

    @Step("Отправка запроса на выход пользователя")
    public Response logout (Response response) {

        UserCreds userCreds = response.as(UserCreds.class);
        tokenForAuth = new TokenForAuth(userCreds.getRefreshToken());
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(tokenForAuth)
                .when()
                .post(LOGOUT_USER_PATH);
    }

    @Step("Отправка запроса на вход пользователя")
    public Response login (User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(LOGIN_USER_PATH);
    }

    @Step("Отправка запроса на вход пользователя с неправильным паролем")
    public Response loginUserWithIncorrectPassword (User user) {
        user.setPassword(user.getPassword()+"123");
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(LOGIN_USER_PATH);
    }

    @Step("Отправка запроса на вход пользователя с неправильным email")
    public Response loginUserWithIncorrectEmail (User user) {
        user.setEmail("123" + user.getEmail());
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(LOGIN_USER_PATH);
    }

}