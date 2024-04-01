package org.eclipse.jakarta.model.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.eclipse.jakarta.model.entity.TaxiRide;

import java.util.List;
import java.util.Optional;

@Stateless
public class TaxiRideRepository {

    @PersistenceContext
    private EntityManager em;
    
    public TaxiRide create(TaxiRide taxiRide) {
        em.persist(taxiRide);

        return taxiRide;
    }

    public List<TaxiRide> findAll() {
        return em.createQuery("SELECT t FROM TaxiRide t", TaxiRide.class).getResultList();
    }

    public Optional<TaxiRide> findById(Long id) {
        return Optional.ofNullable(em.find(TaxiRide.class, id));
    }

    public void delete(Long id) {
        TaxiRide taxiRide = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid taxiRide Id:" + id));
        em.remove(taxiRide);
    }

    public TaxiRide update(TaxiRide taxiRide) {
        return em.merge(taxiRide);
    }
}
