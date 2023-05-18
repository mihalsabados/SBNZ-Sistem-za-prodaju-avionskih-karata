package com.ftn.sbnz.repository;

import com.ftn.sbnz.model.Flight;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightRepository extends MongoRepository<Flight, Long> {
    Optional<Flight> findById(Long id);
}
