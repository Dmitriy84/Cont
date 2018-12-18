package com.continuum.cucumber.be;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Builder
public class AuthData {
    private String authId;
    private String template;
    private String stage;
    private String header;
    private List<Callback> callbacks;

    public static AuthData defaultData(String token, String email, String password) {
        return AuthData
                .builder()
                .authId(token)
                .template("")
                .stage("IDMDataStore1")
                .header("Access ITSupport247 Account")
                .callbacks(new ArrayList<Callback>() {{
                    add(createCallback("NameCallback", "prompt", "Email ID:", "IDToken1", email));
                    add(createCallback("PasswordCallback", "prompt", "Password:", "IDToken2", password));
                    add(createCallback("TextOutputCallback", "message", "\n", "messageType", "0"));
                }})
                .build();
    }

    private static Callback createCallback(String type, String oName, String oValue, String iName, String iValue) {
        return Callback
                .builder()
                .type(type)
                .output(createListNameValue(oName, oValue))
                .input(createListNameValue(iName, iValue))
                .build();
    }

    private static List<NameValue> createListNameValue(String name, String value) {
        return Collections.singletonList(NameValue
                .builder()
                .name(name)
                .value(value)
                .build()
        );
    }
}

@Data
@Builder
class Callback {
    private String type;
    private List<NameValue> output;
    private List<NameValue> input;
}

@Data
@Builder
class NameValue {
    private String name;
    private String value;
}