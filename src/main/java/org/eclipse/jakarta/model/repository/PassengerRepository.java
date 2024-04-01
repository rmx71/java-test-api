package org.eclipse.jakarta.model.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.eclipse.jakarta.model.entity.Passenger;

import java.util.List;
import java.util.Optional;

@Stateless
public class PassengerRepository {

    @PersistenceContext
    private EntityManager em;

    public Passenger create(Passenger passenger) {
        em.persist(passenger);

        return passenger;
    }

    public List<Passenger> findAll() {
        return em.createNamedQuery("Passenger.findAll", Passenger.class).getResultList();
    }

    public Optional<Passenger> findById(Long id) {
        return Optional.ofNullable(em.find(Passenger.class, id));
    }

    public void delete(Long id) {
        Passenger passenger = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid passenger Id:" + id));
        em.remove(passenger);
    }

    public Passenger update(Passenger passenger) {
        return em.merge(passenger);
    }
}
