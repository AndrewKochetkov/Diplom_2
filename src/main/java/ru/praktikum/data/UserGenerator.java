package ru.praktikum.data;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

import java.util.Locale;

public class UserGenerator {
    private static String email ;
    private static String password;
    private static String name;
    public static User getUser() {
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-US"), new RandomService());

        email = fakeValuesService.bothify("?????@gmail.com");
        password = fakeValuesService.bothify("??????");
        name = fakeValuesService.bothify("??????");
        return new User(email, password, name);
    }
}