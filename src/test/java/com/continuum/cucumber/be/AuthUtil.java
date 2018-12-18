package com.continuum.cucumber.be;

import com.continuum.cucumber.shell.Shell;
import lombok.Getter;
import lombok.Setter;
import lombok.var;

import static io.restassured.RestAssured.given;

public class AuthUtil {
    @Getter
    @Setter
    private static String token;

    public static void logout() {
        given().baseUri(Shell.Application.getEnvironment().getAuthUrl() + "sessions/?_action=logout").then().statusCode(200);
        token = null;
    }

    public static String generateToken(String email, String pass) {
        String uri = Shell.Application.getEnvironment().getAuthUrl() + "ITSUPPORT247DATASTORE/authenticate";
        String authId = given().baseUri(uri).post().getBody().jsonPath().get("authId");
        return given().baseUri(uri).body(AuthData.defaultData(authId, email, pass))
                .header("Accept", "application/json").header("Content-Type", "application/json")
                .post().getBody().jsonPath().get("tokenId");
    }

    public static void setup() {
        if (getToken() == null) {
            var env = Shell.Application.getEnvironment();
            setToken(generateToken(env.getUser(), env.getPass()));
        }
    }
}