package com.ftn.sbnz.repository;

import com.ftn.sbnz.model.LastMinuteEvent;
import com.ftn.sbnz.model.RedundantPaymentEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedundantPaymentEventRepository extends MongoRepository<RedundantPaymentEvent, Long> {
}