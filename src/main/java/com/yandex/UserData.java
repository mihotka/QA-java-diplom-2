package com.yandex;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

public class UserData {

    public final String email;
    public final String password;
    public final String name;

    public UserData(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static UserData getRandom() {
        Faker faker = new Faker();
        final String email = faker.name().firstName() + "@gmail.su";
        final String password = RandomStringUtils.randomAlphabetic(10);
        final String name = faker.name().firstName();
        return new UserData(email, password, name);
    }

}
