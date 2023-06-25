package org.bhavani.constructions.resources;


import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.entities.DriverEntity;
import org.bhavani.constructions.dao.entities.EmployeeAttendanceEntity;
import org.bhavani.constructions.dao.entities.VendorAttendanceEntity;
import org.bhavani.constructions.dao.entities.models.AttendanceType;
import org.bhavani.constructions.dao.entities.models.TransactionMode;
import org.bhavani.constructions.dto.CreateDriverRequestDTO;
import org.bhavani.constructions.dto.CreateEmployeeAttendanceRequestDTO;
import org.bhavani.constructions.dto.CreateVendorAttendanceRequestDTO;
import org.bhavani.constructions.services.DriverService;
import org.bhavani.constructions.services.EmployeeAttendanceService;
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
import java.util.EnumSet;
import java.util.List;

import static org.bhavani.constructions.constants.Constants.X_USER_ID;

@Path("/drivers")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
@Api(value = "drivers")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class DriverResource {
    private final DriverService driverService;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getDrivers(@NotNull @HeaderParam(X_USER_ID) String userId){
        List<CreateDriverRequestDTO> drivers = driverService.getDrivers();
        return Response.ok(drivers).build();
    }

    @GET
    @Path("/names")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getDriverNames(@NotNull @HeaderParam(X_USER_ID) String userId){
        List<String> driverNames = driverService.getDriverNames();
        return Response.ok(driverNames).build();
    }

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
