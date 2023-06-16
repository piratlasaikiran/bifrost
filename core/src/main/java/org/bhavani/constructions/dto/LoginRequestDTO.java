package org.bhavani.constructions.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class LoginRequestDTO{

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;
}
