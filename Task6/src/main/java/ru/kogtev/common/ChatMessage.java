package ru.kogtev.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("message")
public class ChatMessage extends Message {
    private final String sender;
    private final String content;

    @JsonCreator
    public ChatMessage(@JsonProperty("sender") String sender, @JsonProperty("content") String content) {
        this.sender = sender;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }
}

