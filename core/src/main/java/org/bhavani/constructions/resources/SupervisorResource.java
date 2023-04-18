package org.bhavani.constructions.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.entities.SupervisorEntity;
import org.bhavani.constructions.dto.CreateSupervisorRequestDTO;
import org.bhavani.constructions.services.SupervisorService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.bhavani.constructions.constants.Constants.X_USER_ID;

@Path("/supervisors")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
@Api(value = "supervisors")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class SupervisorResource {
    private final SupervisorService supervisorService;

    @GET
    @Path("/{employeeName}")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getSupervisor(@Nonnull @PathParam("employeeName") String employeeName){
        SupervisorEntity supervisor = supervisorService.getEmployee(employeeName);
        return Response.ok(supervisor).build();
        }

    @POST
    @Path("/create-new-employee")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response saveSupervisor(@Valid CreateSupervisorRequestDTO createSupervisorRequestDTO,
                                   @NotNull @HeaderParam(X_USER_ID) String userId){
        SupervisorEntity supervisor = supervisorService.createEmployee(createSupervisorRequestDTO, userId);
        return Response.ok(supervisor).build();
    }

    @PUT
    @Path("/update-employee")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response updateSupervisor(@Valid CreateSupervisorRequestDTO createSupervisorRequestDTO,
                                   @NotNull @HeaderParam(X_USER_ID) String userId){
        SupervisorEntity supervisor = supervisorService.updateEmployee(createSupervisorRequestDTO, userId);
        return Response.ok(supervisor).build();
    }

    @DELETE
    @Path("/{employeeName}")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public String deleteSupervisor(@Nonnull @PathParam("employeeName") String employeeName){
        supervisorService.deleteEmployee(employeeName);
        return String.format("Employee: %s deleted successfully", employeeName);
    }
}
