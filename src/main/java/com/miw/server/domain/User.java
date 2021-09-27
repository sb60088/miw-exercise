package com.miw.server.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    private String name;

    public User(String name) {
        this.name = name;
    }
}
