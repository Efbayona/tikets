package com.example.tickets.ticket.service.impl;

import com.example.tickets.common.criteria.Criteria;
import com.example.tickets.common.criteria.Filters;
import com.example.tickets.common.criteria.Order;
import com.example.tickets.common.exception_handler.ResourceNotFoundException;
import com.example.tickets.common.object.SearchByCriteria;
import com.example.tickets.common.parse.ParseFilters;
import com.example.tickets.ticket.dto.CreateTicketRequest;
import com.example.tickets.ticket.dto.TicketResponse;
import com.example.tickets.ticket.entity.Ticket;
import com.example.tickets.ticket.repository.TicketRepository;
import com.example.tickets.ticket.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public void createTicket(CreateTicketRequest request) {
        ticketRepository.save(
                new Ticket(
                        request.getUser(),
                        request.getStatus()
                )
        );
    }

    @Override
    public Page<TicketResponse> getAllTickets(SearchByCriteria search, String quickSearch) {

        Order order = Order.fromValues(search.orderBy(), search.orderType());

        if (!order.hasOrder()) {
            order = Order.desc("created_at");
        }

        Criteria criteria = new Criteria(
                new Filters(ParseFilters.getFilters(search.filters())),
                order,
                search.limit(),
                search.offset()
        );

        Criteria criteriaCount = new Criteria(new Filters(ParseFilters.getFilters(search.filters())), Order.none());
        return ticketRepository.getAllTickets(criteria, ticketRepository.countTickets(criteriaCount, quickSearch), quickSearch);
    }


    @Override
    public TicketResponse getTicketById(UUID id) {
        TicketResponse response = ticketRepository.getTicketById(id);
        if (response == null) {
            throw new ResourceNotFoundException("El ticket solicitado no está registrado.");
        }
        return response;
    }

    @Override
    public TicketResponse updateTicket(UUID id, CreateTicketRequest request) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El ticket solicitado no está registrado."));

        ticket.update(
                request.getUser(),
                request.getStatus()
        );
        ticket = ticketRepository.save(ticket);

        return new TicketResponse(
                ticket.getId(),
                ticket.getUserName(),
                ticket.getCreatedAt(),
                ticket.getUpdateAt(),
                ticket.getStatus()
        );
    }

    @Override
    public void deleteTicket(UUID id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El ticket solicitado no está registrado."));
        ticketRepository.delete(ticket);
    }
}