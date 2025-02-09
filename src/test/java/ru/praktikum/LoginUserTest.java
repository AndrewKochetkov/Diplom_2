package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.data.User;
import ru.praktikum.data.UserGenerator;
import ru.praktikum.user.*;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static ru.praktikum.data.Const.LOGIN_USER_ERROR;

public class LoginUserTest {
    private User user;
    private UserAction userAction;
    private Response response;
    private String userEmail;
    private String userPassowrd;
    private UserCreate userCreate = new UserCreate();

    @Before
    public void setUp() {
        user = UserGenerator.getUser(); // Генерация пользователя
        userAction = new UserAction(); // Создание объекта для работы с пользователем
        userEmail = user.getEmail();
        userPassowrd = user.getPassword();
        userAction.logout(userCreate.createUser(user)); // Создание пользователя и выход из системы
    }

    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Тест проверяет успешную авторизацию пользователя с корректными данными")
    public void loginUserTest() {
        response = userAction.login(user);
        response.then()
                .assertThat().body("success", equalTo(true))
                .and().statusCode(SC_OK)
                .and().assertThat().body("user.email", equalTo(user.getEmail()))
                .and().assertThat().body("user.name", equalTo(user.getName()));
    }

    @Test
    @DisplayName("Авторизация пользователя с некорректным паролем")
    @Description("Тест проверяет, что при вводе некорректного пароля пользователь не может авторизоваться")
    public void loginUserWithIncorrectPasswordTest() {
        response = userAction.loginUserWithIncorrectPassword(user);
        response.then()
                .assertThat().body("success", equalTo(false))
                .and().statusCode(SC_UNAUTHORIZED)
                .and().assertThat().body("message", equalTo(LOGIN_USER_ERROR));
    }

    @Test
    @DisplayName("Авторизация пользователя с некорректным email")
    @Description("Тест проверяет, что при вводе некорректного email пользователь не может авторизоваться")
    public void loginUserWithIncorrectEmailTest() {
        response = userAction.loginUserWithIncorrectEmail(user);
        response.then()
                .assertThat().body("success", equalTo(false))
                .and().statusCode(SC_UNAUTHORIZED)
                .and().assertThat().body("message", equalTo(LOGIN_USER_ERROR));
    }

    @After
    public void tearDown() {
        user.setEmail(userEmail);
        user.setPassword(userPassowrd);
        userAction.deleteUser(userAction.login(user)); // Удаление тестового пользователя
    }
}
