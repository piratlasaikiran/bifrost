package org.bhavani.constructions.resources;


import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.entities.DriverEntity;
import org.bhavani.constructions.dto.CreateDriverRequestDTO;
import org.bhavani.constructions.services.DriverService;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

import static org.bhavani.constructions.constants.Constants.X_USER_ID;

@Path("/drivers")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
@Api(value = "drivers")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class DriverResource {
    private final DriverService driverService;

    @POST
    @Path("/create-new-driver")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @UnitOfWork
    public Response createDriver(@FormDataParam("createDriverPayload") CreateDriverRequestDTO createDriverRequestDTO,
                                   @FormDataParam("license") InputStream license,
                                   @FormDataParam("license") FormDataContentDisposition licenseContent,
                                   @FormDataParam("aadhar") InputStream aadhar,
                                   @FormDataParam("aadhar") FormDataContentDisposition aadharContent,
                                   @NotNull @HeaderParam(X_USER_ID) String userId){
        DriverEntity driver = driverService.createDriver(createDriverRequestDTO, license, aadhar, userId);
        return Response.ok(driverService.createDriverResponse(driver)).build();
    }

    @PUT
    @Path("/{driverName}/update-driver")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @UnitOfWork
    public Response updateDriver(@Nonnull @PathParam("driverName") String driverName,
                                 @FormDataParam("createDriverPayload") CreateDriverRequestDTO createDriverRequestDTO,
                                 @FormDataParam("license") InputStream license,
                                 @FormDataParam("license") FormDataContentDisposition licenseContent,
                                 @FormDataParam("aadhar") InputStream aadhar,
                                 @FormDataParam("aadhar") FormDataContentDisposition aadharContent,
                                 @NotNull @HeaderParam(X_USER_ID) String userId){
        DriverEntity driver = driverService.updateDriver(createDriverRequestDTO, license, aadhar, userId);
        return Response.ok(driverService.createDriverResponse(driver)).build();
    }

    @GET
    @Path("/{driverName}/get-driver")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getDriver(@Nonnull @PathParam("driverName") String driverName){
        DriverEntity driverEntity = driverService.getDriver(driverName);
        return Response.ok(driverEntity).build();
    }

    @DELETE
    @Path("/{driverName}/delete-driver")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public String deleteDriver(@Nonnull @PathParam("driverName") String driverName){
        driverService.deleteDriver(driverName);
        return String.format("Driver: %s deleted successfully", driverName);
    }

}
