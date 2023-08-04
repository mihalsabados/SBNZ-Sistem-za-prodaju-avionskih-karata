package com.ftn.sbnz.repository;

import com.ftn.sbnz.model.PriceTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceTemplateRepository extends MongoRepository<PriceTemplate, Long> {
}
