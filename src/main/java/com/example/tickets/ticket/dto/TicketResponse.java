package com.example.tickets.ticket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class TicketResponse {

    @JsonProperty(value = "id")
    private UUID id;

    @JsonProperty(value = "user")
    private String user;

    @JsonProperty(value = "create_at")
    private LocalDateTime createAt;

    @JsonProperty(value = "update_at")
    private LocalDateTime updateAt;

    @JsonProperty(value = "status")
    private String status;

    public TicketResponse(UUID id, String user, LocalDateTime createAt, LocalDateTime updateAt, String status) {
        this.id = id;
        this.user = user;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.status = status;
    }
}
