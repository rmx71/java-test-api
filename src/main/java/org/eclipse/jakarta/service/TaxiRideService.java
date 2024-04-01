package org.eclipse.jakarta.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.jakarta.model.entity.Driver;
import org.eclipse.jakarta.model.entity.Passenger;
import org.eclipse.jakarta.model.entity.TaxiRide;
import org.eclipse.jakarta.model.repository.PassengerRepository;
import org.eclipse.jakarta.model.repository.TaxiRideRepository;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Stateless
public class TaxiRideService {
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Inject
    TaxiRideRepository taxiRideRepository;

    @Inject
    PassengerRepository passengerRepository;

    @PersistenceContext
    private EntityManager em;

    public TaxiRide create(TaxiRide taxiRide) {
        logger.info("Creating taxiRide record");
        if (taxiRide.getDriver().getId() == null || taxiRide.getPassengers() == null || taxiRide.getPassengers().isEmpty()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        LocalDateTime dateTime = LocalDateTime.now();
        if (taxiRide.getDateTime() == null) {
            taxiRide.setDateTime(dateTime);
        }
        return taxiRideRepository.create(taxiRide);
    }

    public TaxiRide update(TaxiRide taxiRide) {
        logger.info("Updating taxiRide record");
        taxiRideRepository.findById(taxiRide.getId()).orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
        return taxiRideRepository.update(taxiRide);
    }

    public TaxiRide deletePassenger(long taxiRideId, long passengerId) {
        logger.info("Deleting passenger from taxiRide record with id: " + taxiRideId + " and passenger id: " + passengerId);
        TaxiRide taxiRide = taxiRideRepository.findById(taxiRideId).orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
        Passenger passenger = passengerRepository.findById(passengerId).orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND) );
        taxiRide.getPassengers().remove(passenger);
        return taxiRideRepository.update(taxiRide);
    }

    public void delete(long id) {
        logger.info("Deleting taxiRide record with id: " + id + "");
        taxiRideRepository.findById(id).orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
        taxiRideRepository.delete(id);
    }

    public Optional<TaxiRide> findById(Long id) {
        logger.info("Find taxiRide by id: " + id + "");
        return taxiRideRepository.findById(id);
    }

    public List<TaxiRide> findAll() {
        logger.info("Find all taxiRide records");
        return taxiRideRepository.findAll();
    }
    public List<TaxiRide> findTaxiRideBy(String startDate, String endDate, Double minCost, Double maxCost,
                                       Integer minDuration, Integer maxDuration, Long driverId,
                                       Long passengerId, Integer passengerAge) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<TaxiRide> criteriaQuery = criteriaBuilder.createQuery(TaxiRide.class);
        Root<TaxiRide> root = criteriaQuery.from(TaxiRide.class);

        List<Predicate> predicates = new ArrayList<>();

        if (startDate != null && endDate != null) {
            logger.info("startDate: " + startDate + " endDate: " + endDate);
            LocalDateTime startDateTime = formatStartDateTime(startDate);
            LocalDateTime  endDateTime = formatEndDateTime(endDate);
            logger.info("startDateTime: " + startDateTime + " endDateTime: " + endDateTime);
            predicates.add(criteriaBuilder.between(root.get("dateTime"), startDateTime, endDateTime));
        }
        if (minCost != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("cost"), minCost));
        }
        if (maxCost != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("cost"), maxCost));
        }
        if (minDuration != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("duration"), minDuration));
        }
        if (maxDuration != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("duration"), maxDuration));
        }
        if (driverId != null) {
            Join<TaxiRide, Driver> driverJoin = root.join("driver");
            predicates.add(criteriaBuilder.equal(driverJoin.get("id"), driverId));
        }
        if (passengerId != null) {
            Join<TaxiRide, Passenger> passengerJoin = root.join("passengers");
            predicates.add(criteriaBuilder.equal(passengerJoin.get("id"), passengerId));
        }
        if (passengerAge != null) {
            Join<TaxiRide, Passenger> passengerJoin = root.join("passengers");
            predicates.add(criteriaBuilder.lessThanOrEqualTo(passengerJoin.get("age"), passengerAge));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        criteriaQuery.select(root);

        List<TaxiRide> taxiRides = em.createQuery(criteriaQuery).getResultList();

        return taxiRides;
    }

    private LocalDateTime formatStartDateTime(String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        LocalDateTime dateTime = date.atStartOfDay();

        return dateTime;
    }

    private LocalDateTime formatEndDateTime(String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        LocalDateTime dateTime = date.atTime(23, 59, 59);

        return dateTime;
    }
}
