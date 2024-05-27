package ru.kogtev;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Set;

@JsonTypeName("USER_LIST")
public class UserListMessage extends Message {
    private final Set<String> users;

    @JsonCreator
    public UserListMessage(@JsonProperty("users") Set<String> users) {
        this.users = users;
    }

    public Set<String> getUsers() {
        return users;
    }
}
