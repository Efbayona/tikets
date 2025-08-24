package com.example.tickets.ticket.controller;

import com.example.tickets.common.object.SearchByCriteria;
import com.example.tickets.common.parse.ParseFilters;
import com.example.tickets.ticket.dto.CreateTicketRequest;
import com.example.tickets.ticket.dto.TicketResponse;
import com.example.tickets.ticket.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService ticketService ;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/create")
    @Operation(description = "Create a new ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ticket created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    public ResponseEntity<HttpStatus> createTicket(@Valid @RequestBody CreateTicketRequest request) {
        ticketService.createTicket(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/")
    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(description = "Get all tickets")
    public ResponseEntity<Page<TicketResponse>> getAllTickets(@RequestParam Map<String, Serializable> params) {
        SearchByCriteria search = new SearchByCriteria(
                ParseFilters.parseFilters(params),
                Optional.ofNullable((String) params.get("order_by")),
                Optional.ofNullable((String) params.get("order")),
                ParseFilters.serializableToOptionalInteger(params.get("limit")),
                ParseFilters.serializableToOptionalInteger(params.get("offset"))
        );
        String quickSearch = params.get("quick_search") != null ? params.get("quick_search").toString() : null;
        return new ResponseEntity<>(ticketService.getAllTickets(search, quickSearch), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(description = "Get ticket by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket found"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable UUID id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @PutMapping("/{id}")
    @Operation(description = "Update ticket by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket updated"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    public ResponseEntity<TicketResponse> updateTicket(@PathVariable UUID id, @Valid @RequestBody CreateTicketRequest request) {
        return ResponseEntity.ok(ticketService.updateTicket(id, request));
    }


    @DeleteMapping("/{id}")
    @Operation(description = "Delete ticket by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ticket deleted"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    public ResponseEntity<HttpStatus> deleteTicket(@PathVariable UUID id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}
