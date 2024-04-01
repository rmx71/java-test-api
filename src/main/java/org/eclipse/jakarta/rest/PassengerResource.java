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
import org.eclipse.jakarta.model.entity.Passenger;
import org.eclipse.jakarta.service.PassengerService;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.logging.Logger;

@Path("passengers")
public class PassengerResource {


    private final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Inject
    private PassengerService passengerService;

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Passenger findPassenger(@PathParam("id") Long id) {
        logger.info("Getting passenger by id " + id);
        return passengerService.findById(id)
                .orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
    }

    @GET
    @Produces("application/json")
    public List<Passenger> findAll() {
        logger.info("Getting all passenger");
        return passengerService.findAll();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Passenger create(Passenger passenger) {
        logger.info("Creating passenger " + passenger.getFirstName() + " " + passenger.getLastName());
        try{
            return passengerService.create(passenger);
        }catch (PersistenceException ex){
            logger.info("Error creating passenger " + passenger.getFirstName() + " " + passenger.getLastName());
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        logger.info("Deleting passenger by id " + id);
        try{
            passengerService.delete(id);
        }catch (IllegalArgumentException e){
            logger.info("Error deleting passenger by id " + id);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public Passenger update(Passenger passenger) {
        logger.info("Creating passenger " + passenger.getFirstName() + " " + passenger.getLastName());
        try{
            return passengerService.update(passenger);
        }catch (PersistenceException ex){
            logger.info("Error updating passenger " + passenger.getFirstName() + " " + passenger.getLastName());
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }
}
