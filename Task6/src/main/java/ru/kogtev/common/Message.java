package ru.kogtev.common;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UserListMessage.class, name = "userList"),
        @JsonSubTypes.Type(value = ChatMessage.class, name = "message")
})
public abstract class Message {

}
