package org.eclipse.jakarta.model.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.eclipse.jakarta.model.entity.Driver;

import java.util.List;
import java.util.Optional;

@Stateless
public class DriverRepository {
    @PersistenceContext
    private EntityManager em;

    public Driver create(Driver driver) {
        em.persist(driver);

        return driver;
    }

    public List<Driver> findAll() {
        return em.createNamedQuery("Driver.findAll", Driver.class).getResultList();
    }

    public Optional<Driver> findById(Long id) {
        return Optional.ofNullable(em.find(Driver.class, id));
    }

    public void delete(Long id) {
        Driver driver = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid driver Id:" + id));
        em.remove(driver);
    }

    public Driver update(Driver driver) {
        return em.merge(driver);
    }
}
