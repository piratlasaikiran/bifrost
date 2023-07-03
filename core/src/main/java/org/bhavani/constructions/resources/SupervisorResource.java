package org.bhavani.constructions.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.entities.SupervisorEntity;
import org.bhavani.constructions.dto.CreateSupervisorRequestDTO;
import org.bhavani.constructions.services.SupervisorService;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

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
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getSupervisors(@NotNull @HeaderParam(X_USER_ID) String userId){
        List<CreateSupervisorRequestDTO> supervisors = supervisorService.getSupervisors();
        return Response.ok(supervisors).build();
    }

    @GET
    @Path("/get-supervisor-names")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getSupervisorNames(@NotNull @HeaderParam(X_USER_ID) String userId){
        List<CreateSupervisorRequestDTO> supervisors = supervisorService.getSupervisors();
        return Response.ok(supervisors.stream()
                .map(CreateSupervisorRequestDTO::getName)
                .collect(Collectors.toList())).build();
    }

    @GET
    @Path("/{supervisorName}/get-supervisor")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getSupervisor(@Nonnull @PathParam("supervisorName") String supervisorName){
        SupervisorEntity supervisor = supervisorService.getSupervisor(supervisorName);
        return Response.ok(supervisor).build();
    }

    @GET
    @Path("/{supervisorName}/get-aadhar")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getAadhar(@Nonnull @PathParam("supervisorName") String supervisorName){
        InputStream aadhar = new ByteArrayInputStream(supervisorService.getSupervisor(supervisorName).getAadhar());
        return Response.ok(aadhar).build();
    }

    @POST
    @Path("/create-new-supervisor")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response saveSupervisor(@FormDataParam("createSupervisorPayload") CreateSupervisorRequestDTO createSupervisorRequestDTO,
                                   @FormDataParam("aadhar") InputStream aadhar,
                                   @FormDataParam("aadhar") FormDataContentDisposition aadharContent,
                                   @NotNull @HeaderParam(X_USER_ID) String userId){
        SupervisorEntity supervisor = supervisorService.createSupervisor(createSupervisorRequestDTO, aadhar, userId);
        return Response.ok(supervisorService.createSupervisorResponse(supervisor)).build();
    }

    @PUT
    @Path("/{supervisorName}/update-supervisor")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response updateSupervisor(@Nonnull @PathParam("supervisorName") String supervisorName,
                                     @FormDataParam("createSupervisorPayload") CreateSupervisorRequestDTO createSupervisorRequestDTO,
                                     @FormDataParam("aadhar") InputStream aadhar,
                                     @FormDataParam("aadhar") FormDataContentDisposition aadharContent,
                                     @NotNull @HeaderParam(X_USER_ID) String userId){
        SupervisorEntity supervisor = supervisorService.updateSupervisor(createSupervisorRequestDTO, aadhar, userId, supervisorName);
        return Response.ok(supervisorService.createSupervisorResponse(supervisor)).build();
    }

    @DELETE
    @Path("/{supervisorName}/delete-supervisor")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public String deleteSupervisor(@Nonnull @PathParam("supervisorName") String supervisorName){
        supervisorService.deleteSuperVisor(supervisorName);
        return String.format("Supervisor: %s deleted successfully", supervisorName);
    }
}
