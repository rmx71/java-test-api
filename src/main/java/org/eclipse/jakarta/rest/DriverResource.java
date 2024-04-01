package org.eclipse.jakarta.rest;

import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.jakarta.model.entity.Driver;
import org.eclipse.jakarta.service.DriverService;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.logging.Logger;

@Path("drivers")
public class DriverResource {

    private final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Inject
    private DriverService driverService;

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Driver findDriver(@PathParam("id") Long id) {
        logger.info("Getting driver by id " + id);
        return driverService.findById(id).orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
    }

    @GET
    @Produces("application/json")
    public List<Driver> findAll() {
        logger.info("Getting all driver");
        return driverService.findAll();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Driver create(Driver driver) {
        logger.info("Creating driver " + driver.getName());
        try{
            return driverService.create(driver);
        }catch (PersistenceException ex){
            logger.info("Error creating driver " + driver.getName());
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        logger.info("Deleting driver by id " + id);
        try{
            driverService.delete(id);
        }catch (IllegalArgumentException e){
            logger.info("Error deleting driver by id " + id);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }


    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public Driver update(Driver driver) {
        logger.info("Updating driver " + driver.getName());
        try{
            return driverService.update(driver);
        } catch (PersistenceException ex){
            logger.info("Error updating driver " + driver.getName());
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }
}
