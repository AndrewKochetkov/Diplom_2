package ru.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.data.User;
import ru.praktikum.data.UserGenerator;
import ru.praktikum.user.*;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static ru.praktikum.data.Const.USER_DONT_AUTH_ERROR;
import static ru.praktikum.data.Const.USER_EMAIL_EXISTS_ERROR;

public class ChangeUserInfoTest {
    private User user;
    private User userSecond;
    private UserChange userChange;
    private UserAction userAction;
    private Response response;

    @Before
    public void setUp() {
        user = UserGenerator.getUser();
        userChange = new UserChange();
        userAction = new UserAction();
    }

    @Test
    @DisplayName("Изменение email пользователя с авторизацией")
    @Description("Тест проверяет возможность изменения email пользователя при наличии авторизации")
    public void changeUserEmailWithAuthTest(){
        response = userChange.changeEmailUserWithAuth(user);
        response.then()
                .assertThat().body("success", equalTo(true))
                .and().assertThat().body("user.email", equalTo("test"+user.getEmail()))
                .and().assertThat().body("user.name", equalTo(user.getName()))
                .and().statusCode(SC_OK);
        user.setEmail("test"+user.getEmail());
    }

    @Test
    @DisplayName("Изменение имени пользователя с авторизацией")
    @Description("Тест проверяет возможность изменения имени пользователя при наличии авторизации")
    public void changeUserNameWithAuthTest(){
        response = userChange.changeNameUserWithAuth(user);
        response.then()
                .assertThat().body("success", equalTo(true))
                .and().assertThat().body("user.email", equalTo(user.getEmail()))
                .and().assertThat().body("user.name", equalTo("test"+user.getName()))
                .and().statusCode(SC_OK);
        user.setName("test"+user.getEmail());
    }

    @Test
    @DisplayName("Изменение email пользователя без авторизации")
    @Description("Тест проверяет, что попытка изменения email пользователя без авторизации завершится ошибкой")
    public void changeUserEmailWithoutAuthTest(){
        response = userChange.changeEmailUserWithoutAuth(user);
        response.then()
                .assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo(USER_DONT_AUTH_ERROR))
                .and().statusCode(SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Изменение имени пользователя без авторизации")
    @Description("Тест проверяет, что попытка изменения имени пользователя без авторизации завершится ошибкой")
    public void changeUserNameWithoutAuthTest(){
        response = userChange.changeNameUserWithoutAuth(user);
        response.then()
                .assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo(USER_DONT_AUTH_ERROR))
                .and().statusCode(SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Изменение email (на уже существующий) пользователя с авторизацией")
    @Description("Тест проверяет, что попытка изменения email на уже существующий email завершится ошибкой")
    public void changeExistEmailWithAuthTest(){
        String emailFirstUser = user.getEmail();
        userSecond = UserGenerator.getUser();

        response = userChange.changeExistEmailWithAuth(user, userSecond, userSecond.getEmail());
        response.then()
                .assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo(USER_EMAIL_EXISTS_ERROR))
                .and().statusCode(SC_FORBIDDEN);

        user.setEmail(emailFirstUser);
        userAction.deleteUser(userAction.login(userSecond));
    }

    @After
    public void tearDown(){
        userAction.deleteUser(userAction.login(user));
    }
}