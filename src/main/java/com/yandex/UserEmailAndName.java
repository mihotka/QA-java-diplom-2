package com.yandex;


public class UserEmailAndName {
        public String name;
        public String email;

        public UserEmailAndName(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public static UserEmailAndName from(UserData userData) {
            final String email = userData.email;
            final String name = userData.name;
            return new UserEmailAndName(email, name);
        }
}
