package com.anma;

public class TokenService {

    public static getToken(username, password) {
        return new String(Base64.encoder.encode("${username}:${password}".bytes))
    }
}
