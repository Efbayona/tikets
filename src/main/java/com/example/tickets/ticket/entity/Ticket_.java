package com.example.tickets.ticket.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

import javax.annotation.processing.Generated;
import java.time.LocalDateTime;
import java.util.UUID;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Ticket.class)
public abstract class Ticket_ {

    /**
     * ID del ticket.
     */
    public static volatile SingularAttribute<Ticket, UUID> id;

    /**
     * Usuario que creó el ticket.
     */
    public static volatile SingularAttribute<Ticket, String> userName;

    /**
     * Estado del ticket.
     */
    public static volatile SingularAttribute<Ticket, String> status;

    /**
     * Fecha de creación del ticket.
     */
    public static volatile SingularAttribute<Ticket, LocalDateTime> createdAt;

    /**
     * Fecha de última actualización del ticket.
     */
    public static volatile SingularAttribute<Ticket, LocalDateTime> updateAt;
}
