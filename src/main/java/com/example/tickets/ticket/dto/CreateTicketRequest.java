package com.example.tickets.ticket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CreateTicketRequest {

    @JsonProperty(value = "user", required = true)
    private String user;

    @JsonProperty(value = "status",required = true)
    private String status;

}
