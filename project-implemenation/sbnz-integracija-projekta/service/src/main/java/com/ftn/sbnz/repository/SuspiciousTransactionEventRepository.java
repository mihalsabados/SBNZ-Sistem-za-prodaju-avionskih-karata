package com.ftn.sbnz.repository;

import com.ftn.sbnz.model.RedundantPaymentEvent;
import com.ftn.sbnz.model.SuspiciousTransactionEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuspiciousTransactionEventRepository extends MongoRepository<SuspiciousTransactionEvent, Long> {
}