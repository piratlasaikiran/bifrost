package org.bhavani.constructions.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordHelper {

    public static String hashPassword(String plainPassword) {
        String salt = BCrypt.gensalt(12);
        return BCrypt.hashpw(plainPassword, salt);
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    @Getter
    public static class LoginRequest{

        @JsonProperty("username")
        private String username;

        @JsonProperty("password")
        private String password;
    }
}
