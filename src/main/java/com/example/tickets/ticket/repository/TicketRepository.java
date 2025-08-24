package com.example.tickets.ticket.repository;

import com.example.tickets.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> , TicketRepositoryCustom {


}
