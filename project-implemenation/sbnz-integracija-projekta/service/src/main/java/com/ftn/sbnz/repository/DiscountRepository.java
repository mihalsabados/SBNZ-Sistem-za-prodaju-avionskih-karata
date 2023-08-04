package com.ftn.sbnz.repository;

import com.ftn.sbnz.model.Discount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends MongoRepository<Discount, String> {
    Discount findByName(String discountName);
}
