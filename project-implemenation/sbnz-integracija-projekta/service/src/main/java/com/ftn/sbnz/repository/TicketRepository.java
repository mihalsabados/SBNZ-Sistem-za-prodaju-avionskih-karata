package com.ftn.sbnz.repository;

import com.ftn.sbnz.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, Long> {
}
