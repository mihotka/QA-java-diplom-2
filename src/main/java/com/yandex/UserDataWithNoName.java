package com.yandex;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Locale;

public class UserDataWithNoName {
    public final String email;
    public final String name;

    public UserDataWithNoName(String email, String name) {
        this.email = email;
        this.name= name;
    }

    public static UserDataWithNoName getRandom() {
        final String email = "alrt" +  RandomStringUtils.random(5) + "@gmail.su";
        final String name = RandomStringUtils.randomAlphabetic(10).toLowerCase(Locale.ROOT);

        return new UserDataWithNoName(email, name);
    }

}
