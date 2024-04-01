package org.eclipse.jakarta.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.jakarta.model.entity.Passenger;
import org.eclipse.jakarta.model.repository.PassengerRepository;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Stateless
public class PassengerService {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Inject
    PassengerRepository passengerRepository;

    public Passenger create(Passenger passenger) {
        logger.info("Passenger Service: create");
        return passengerRepository.create(passenger);
    }

    public List<Passenger> findAll() {
        logger.info("Passenger Service: findAll");
        return passengerRepository.findAll();
    }

    public Optional<Passenger> findById(Long id) {
        logger.info("Passenger Service: findById");
        return passengerRepository.findById(id);
    }

    public void delete(long id) {
        logger.info("Passenger Service: delete");
        passengerRepository.findById(id).orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
        passengerRepository.delete(id);
    }

    public Passenger update(Passenger passenger) {
        logger.info("Passenger Service: update");
        passengerRepository.findById(passenger.getId()).orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
        return passengerRepository.update(passenger);
    }
}
