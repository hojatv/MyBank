package org.simplebank.domain;

import com.google.gson.JsonElement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Response {
    private JsonElement data;
    private Status status;
    private String message;

    public Response(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public Response(Status status, JsonElement data) {
        this.status = status;
        this.data = data;
    }
}
