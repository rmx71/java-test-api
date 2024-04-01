package org.eclipse.jakarta.rest;

import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.jakarta.model.entity.TaxiRide;
import org.eclipse.jakarta.service.TaxiRideService;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.logging.Logger;

@Path("taxi-rides")
public class TaxiRideResource {

    private final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Inject
    private TaxiRideService taxiRideService;

    @GET
    @Path("{id}")
    @Produces("application/json")
    public TaxiRide findTaxiRide(@PathParam("id") Long id) {
        logger.info("Getting taxiRide by id " + id);
        return taxiRideService.findById(id)
                .orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
    }

    @GET
    @Path("/all")
    @Produces("application/json")
    public List<TaxiRide> findAll() {
        logger.info("Getting all taxiRide");
        return taxiRideService.findAll();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Transactional
    public Response create(TaxiRide taxiRide) {
        logger.info("Creating taxiRide record");
        try{
           taxiRideService.create(taxiRide);
            return Response.ok().build();
        }catch (PersistenceException ex){
            logger.info("Error creating taxiRide record");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        logger.info("Deleting taxiRide by id " + id);
        try{
            taxiRideService.delete(id);
        }catch (IllegalArgumentException e){
            logger.info("Error deleting taxiRide by id " + id);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }


    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public TaxiRide update(TaxiRide taxiRide) {
        logger.info("Updating taxiRide " + taxiRide.getId());
        try{
            return taxiRideService.create(taxiRide);
        }catch (PersistenceException ex){
            logger.info("Error updating taxiRide " + taxiRide.getId());
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }

    @GET
    @Produces("application/json")
    public List<TaxiRide> findTaxiRideBy(@QueryParam("startDate") String startDate,
                                   @QueryParam("endDate") String endDate,
                                   @QueryParam("minCost") Double minCost,
                                   @QueryParam("maxCost") Double maxCost,
                                   @QueryParam("minDuration") Integer minDuration,
                                   @QueryParam("maxDuration") Integer maxDuration,
                                   @QueryParam("byDriver") Long driverId,
                                   @QueryParam("byPassenger") Long passengerId,
                                   @QueryParam("byPassengerAge") Integer passengerAge) {
        logger.info("Getting taxiRide by criteria");
        return taxiRideService.findTaxiRideBy(startDate, endDate, minCost, maxCost, minDuration, maxDuration, driverId,
                passengerId, passengerAge);
    }

    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    @Path("{taxi-ride_id}/{passenger-id}")
    public TaxiRide deletePassenger(@PathParam("taxi-ride_id") long taxiRideId,
                                    @PathParam("passenger-id") long passengerId) {
        logger.info("deletePassenger taxiRide " );
        try{
            return taxiRideService.deletePassenger(taxiRideId, passengerId);
        }catch (PersistenceException ex){
            logger.info("Error updating taxiRide ");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }

}
