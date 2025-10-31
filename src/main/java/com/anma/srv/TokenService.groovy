package com.anma.srv;

public class TokenService {

    public static getToken(username, password) {
        return Base64.encoder.encodeToString("${username}:${password}".bytes)
    }
}
