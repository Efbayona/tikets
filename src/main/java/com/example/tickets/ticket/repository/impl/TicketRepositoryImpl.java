package com.example.tickets.ticket.repository.impl;

import com.example.tickets.common.criteria.Criteria;
import com.example.tickets.common.criteria.CriteriaPredicate;
import com.example.tickets.ticket.dto.TicketResponse;
import com.example.tickets.ticket.entity.Ticket;
import com.example.tickets.ticket.entity.Ticket_;
import com.example.tickets.ticket.repository.TicketRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Repository
@Slf4j
public class TicketRepositoryImpl implements TicketRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<TicketResponse> getAllTickets(Criteria criteria, Long totalRows, String quickSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        Page<TicketResponse> result = null;

        try {
            CriteriaQuery<TicketResponse> cq = cb.createQuery(TicketResponse.class);

            Root<Ticket> root = cq.from(Ticket.class);

            cq.select(cb.construct(
                    TicketResponse.class,
                    root.get(Ticket_.id),
                    root.get(Ticket_.userName),
                    root.get(Ticket_.createdAt),
                    root.get(Ticket_.updateAt),
                    root.get(Ticket_.status)
            ));

            Predicate searchFilter = quickSearchGroups(quickSearch, cb, root);

            CriteriaPredicate<Ticket, TicketResponse> criteriaPredicate = new CriteriaPredicate<>(cb);
            TypedQuery<TicketResponse> query = em.createQuery(criteriaPredicate.convertSearchAndFilters(cq, criteria, root, searchFilter));
            Pageable page = PageRequest.of(criteria.offset().orElse(0), criteria.limit().orElse(15));

            query.setFirstResult(page.getPageNumber() * page.getPageSize());
            query.setMaxResults(page.getPageSize());
            List<TicketResponse> resultList = query.getResultList();

            result = new PageImpl<>(resultList, page, totalRows);
        } catch (Exception ex) {
            log.error("Error en la consulta criteria getAllTickets [{}]", ex.getMessage());
        }
        em.close();
        return result;
    }

    @Override
    public Long countTickets(Criteria criteria, String quickSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        Long totalRows = 0L;

        try {
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);

            Root<Ticket> root = cq.from(Ticket.class);

            cq.select(cb.count(root));

            Predicate searchFilter = quickSearchGroups(quickSearch, cb, root);

            CriteriaPredicate<Ticket, Long> criteriaPredicate = new CriteriaPredicate<>(cb);

            CriteriaQuery<Long> subCriteriaQuery = criteriaPredicate.convertSearchAndFilters(cq, criteria, root, searchFilter);

            totalRows = em.createQuery(subCriteriaQuery).getSingleResult();

        } catch (Exception ex) {
            log.error("error en Criteria countTickets [{}]", ex.getMessage());
        }
        em.close();
        return totalRows;
    }

    @Override
    public TicketResponse getTicketById(UUID ticketId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        TicketResponse response = null;

        try {
            CriteriaQuery<TicketResponse> cq = cb.createQuery(TicketResponse.class);

            Root<Ticket> root = cq.from(Ticket.class);

            cq.select(cb.construct(
                    TicketResponse.class,
                    root.get(Ticket_.id),
                    root.get(Ticket_.userName),
                    root.get(Ticket_.createdAt),
                    root.get(Ticket_.updateAt),
                    root.get(Ticket_.status)
            )).distinct(true).where(cb.equal(root.get(Ticket_.id),ticketId));

            response = em.createQuery(cq).getSingleResult();

        }catch (Exception ex){
            log.error("error en la cosultan getTicketById {}",ex.getMessage());
        }

        return response;
    }


    private Predicate quickSearchGroups(String quickSearch, CriteriaBuilder cb, Root<Ticket> root) {
        Predicate searchFilter = null;

        List<Predicate> predicates = new ArrayList<>();
        if (quickSearch != null) {
            String search = "%" + quickSearch.toLowerCase(Locale.ROOT) + "%";

            predicates.add(cb.like(cb.lower(root.get(Ticket_.userName)), search));
            predicates.add(cb.like(cb.lower(root.get(Ticket_.status)), search));
            searchFilter = cb.or(predicates.toArray(new Predicate[0]));
        }
        return searchFilter;
    }


}