package org.bhavani.constructions.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.entities.EmployeeAttendanceEntity;
import org.bhavani.constructions.dao.entities.models.AttendanceType;
import org.bhavani.constructions.dao.entities.models.EmployeeType;
import org.bhavani.constructions.dto.CreateEmployeeAttendanceRequestDTO;
import org.bhavani.constructions.services.EmployeeAttendanceService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.EnumSet;
import java.util.List;

import static org.bhavani.constructions.constants.Constants.X_USER_ID;

@Path("/employee-attendance")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
@Api(value = "employee-attendance")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class EmployeeAttendanceResource {

    private final EmployeeAttendanceService employeeAttendanceService;

    @POST
    @Path("/enter-attendance")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response enterAttendance(CreateEmployeeAttendanceRequestDTO createEmployeeAttendanceRequestDTO,
                             @NotNull @HeaderParam(X_USER_ID) String userId){
        EmployeeAttendanceEntity employeeAttendanceEntity = employeeAttendanceService.enterAttendance(createEmployeeAttendanceRequestDTO, userId);
        return Response.ok(employeeAttendanceEntity).build();
    }

    @GET
    @Path("/attendance-types")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getAttendanceTypes(@NotNull @HeaderParam(X_USER_ID) String userId){
        EnumSet<AttendanceType> attendanceTypes = employeeAttendanceService.getAttendanceTypes();
        return Response.ok(attendanceTypes).build();
    }

    @GET
    @Path("/employee-types")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getEmployeeTypes(@NotNull @HeaderParam(X_USER_ID) String userId){
        EnumSet<EmployeeType> employeeTypes = employeeAttendanceService.getEmployeeTypes();
        return Response.ok(employeeTypes).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getAllEmployeeAttendances(@NotNull @HeaderParam(X_USER_ID) String userId){
        List<CreateEmployeeAttendanceRequestDTO> createEmployeeAttendanceRequestDTOS = employeeAttendanceService.getAllEmployeeAttendances();
        return Response.ok(createEmployeeAttendanceRequestDTOS).build();
    }

    @PUT
    @Path("/{existingEmployeeAttendanceId}/update-attendance")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response updateAttendance(@Nonnull @PathParam("existingEmployeeAttendanceId") Long existingEmployeeAttendanceId,
                                     CreateEmployeeAttendanceRequestDTO createEmployeeAttendanceRequestDTO,
                                     @NotNull @HeaderParam(X_USER_ID) String userId){
        EmployeeAttendanceEntity employeeAttendanceEntity = employeeAttendanceService.updateAttendance(createEmployeeAttendanceRequestDTO, userId, existingEmployeeAttendanceId);
        return Response.ok(employeeAttendanceEntity).build();
    }
}
