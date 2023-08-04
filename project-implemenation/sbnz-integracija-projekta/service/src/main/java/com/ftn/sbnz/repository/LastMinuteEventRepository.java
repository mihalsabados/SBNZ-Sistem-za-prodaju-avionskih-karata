package com.ftn.sbnz.repository;

import com.ftn.sbnz.model.LastMinuteEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LastMinuteEventRepository extends MongoRepository<LastMinuteEvent, Long> {
}
