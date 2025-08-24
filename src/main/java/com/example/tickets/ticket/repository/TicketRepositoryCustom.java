package com.example.tickets.ticket.repository;

import com.example.tickets.common.criteria.Criteria;
import com.example.tickets.ticket.dto.TicketResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface TicketRepositoryCustom {

    Page<TicketResponse> getAllTickets(Criteria criteria, Long totalRows, String quickSearch);

    Long countTickets(Criteria criteria, String quickSearch);

    TicketResponse getTicketById(UUID ticketId);
}
