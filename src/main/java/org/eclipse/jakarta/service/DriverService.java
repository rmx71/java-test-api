package org.eclipse.jakarta.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.jakarta.model.entity.Driver;
import org.eclipse.jakarta.model.repository.DriverRepository;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Stateless
public class DriverService {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Inject
    DriverRepository driverRepository;

    public Driver create(Driver driver) {
        logger.info("Driver Service: create");
        return driverRepository.create(driver);
    }

    public List<Driver> findAll() {
        logger.info("Driver Service: findAll");
        return driverRepository.findAll();
    }

    public Optional<Driver> findById(Long id) {
        logger.info("Driver Service: findById");
        return driverRepository.findById(id);
    }

    public void delete(Long id) {
        logger.info("Driver Service: delete");
        driverRepository.findById(id).orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
        driverRepository.delete(id);
    }

    public Driver update(Driver driver) {
        logger.info("Driver Service: update");
        driverRepository.findById(driver.getId()).orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
        return driverRepository.update(driver);
    }
}
