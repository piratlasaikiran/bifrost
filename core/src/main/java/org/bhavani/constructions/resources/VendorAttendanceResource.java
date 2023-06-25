package org.bhavani.constructions.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.entities.VendorAttendanceEntity;
import org.bhavani.constructions.dto.CreateVendorAttendanceRequestDTO;
import org.bhavani.constructions.services.VendorAttendanceService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.bhavani.constructions.constants.Constants.X_USER_ID;

@Path("/vendor-attendance")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
@Api(value = "vendor-attendance")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class VendorAttendanceResource {

    private final VendorAttendanceService vendorAttendanceService;

    @POST
    @Path("/enter-attendance")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response saveSite(CreateVendorAttendanceRequestDTO createVendorAttendanceRequestDTO, @NotNull @HeaderParam(X_USER_ID) String userId){
        VendorAttendanceEntity vendorAttendance = vendorAttendanceService.enterAttendance(createVendorAttendanceRequestDTO, userId);
        return Response.ok(vendorAttendance).build();
    }
}
