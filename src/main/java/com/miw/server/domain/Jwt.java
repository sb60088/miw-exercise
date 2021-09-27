package com.miw.server.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Jwt {
    private String token;
    public Jwt(String token){
        this.token = token;
    }
}
