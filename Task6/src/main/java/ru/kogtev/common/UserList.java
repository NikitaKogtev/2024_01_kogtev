package ru.kogtev.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class UserList {
    private final Set<String> users;

    @JsonCreator
    public UserList(@JsonProperty("users") Set<String> users) {
        this.users = users;
    }

    public Set<String> getUsers() {
        return users;
    }
}
