package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.data.User;
import ru.praktikum.user.UserAction;
import ru.praktikum.data.UserGenerator;
import ru.praktikum.user.UserCreate;

import static org.hamcrest.CoreMatchers.equalTo;
import static ru.praktikum.data.Const.CREATE_THE_SAME_USER_ERROR;
import static ru.praktikum.data.Const.CREATE_USER_WITHOUT_FIELD_ERROR;
import static org.apache.http.HttpStatus.*;

public class CreateUserTest {
    private User user;
    private UserAction userAction;
    private UserCreate createUser;
    private Response response;

    @Before
    public void setUp() {
        user = UserGenerator.getUser();
        userAction = new UserAction();
        createUser = new UserCreate();
    }

    @Test
    @DisplayName("Создание пользователя")
    @Description("Тест проверяет, что новый пользователь может быть успешно создан")
    public void createNewUserTest() {
        response = createUser.createUser(user);
        verifySuccessfulUserCreation(response, user);
    }

    @Test
    @DisplayName("Создание уже существующего пользователя")
    @Description("Тест проверяет, что попытка создать уже зарегистрированного пользователя приводит к ошибке")
    public void createTheSameUserTest() {
        createUser.createUser(user);
        response = createUser.createUser(user);
        verifyErrorResponse(response, SC_FORBIDDEN, CREATE_THE_SAME_USER_ERROR);
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Тест проверяет, что нельзя создать пользователя без указания пароля")
    public void createUserWithoutPasswordTest() {
        user.setPassword(null);
        response = createUser.createUser(user);
        verifyErrorResponse(response, SC_FORBIDDEN, CREATE_USER_WITHOUT_FIELD_ERROR);
    }

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Тест проверяет, что нельзя создать пользователя без указания email")
    public void createUserWithoutEmailTest() {
        user.setEmail(null);
        response = createUser.createUser(user);
        verifyErrorResponse(response, SC_FORBIDDEN, CREATE_USER_WITHOUT_FIELD_ERROR);
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Тест проверяет, что нельзя создать пользователя без указания имени")
    public void createUserWithoutNameTest() {
        user.setName(null);
        response = createUser.createUser(user);
        verifyErrorResponse(response, SC_FORBIDDEN, CREATE_USER_WITHOUT_FIELD_ERROR);
    }

    private void verifySuccessfulUserCreation(Response response, User user) {
        response.then()
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true))
                .and().assertThat().body("user.email", equalTo(user.getEmail()))
                .and().assertThat().body("user.name", equalTo(user.getName()));
    }

    private void verifyErrorResponse(Response response, int expectedStatusCode, String expectedMessage) {
        response.then()
                .statusCode(expectedStatusCode)
                .assertThat().body("success", equalTo(false))
                .and().assertThat().body("message", equalTo(expectedMessage));
    }

    @After
    public void tearDown() {
        if (response != null && response.statusCode() == SC_OK && Boolean.TRUE.equals(response.then().extract().path("success"))) {
            userAction.deleteUser(response);
        }
    }
}