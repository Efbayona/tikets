package com.example.tickets.ticket.service;


import com.example.tickets.common.object.SearchByCriteria;
import com.example.tickets.ticket.dto.CreateTicketRequest;
import com.example.tickets.ticket.dto.TicketResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface TicketService {

    void createTicket(CreateTicketRequest request);
    Page<TicketResponse> getAllTickets(SearchByCriteria search, String quickSearch);
    TicketResponse getTicketById(UUID id);
    TicketResponse updateTicket(UUID id, CreateTicketRequest request);
    void deleteTicket(UUID id);

}
