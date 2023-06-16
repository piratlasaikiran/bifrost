package org.bhavani.constructions.resources;


import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.entities.SiteEntity;
import org.bhavani.constructions.dao.entities.models.SiteStatus;
import org.bhavani.constructions.dto.CreateSiteRequestDTO;
import org.bhavani.constructions.services.SiteService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Arrays;
import java.util.List;

import static org.bhavani.constructions.constants.Constants.X_USER_ID;

@Path("/sites")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
@Api(value = "sites")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class SiteResource {
    private final SiteService siteService;

    @POST
    @Path("/create-new-site")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response saveSite(CreateSiteRequestDTO createSiteRequestDTO, @NotNull @HeaderParam(X_USER_ID) String userId){
        SiteEntity siteEntity = siteService.createSite(createSiteRequestDTO);
        return Response.ok(siteEntity).build();
    }

    @GET
    @Path("/{siteName}/get-site")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getSite(@PathParam("siteName") @NotNull String siteName){
        SiteEntity siteEntity = siteService.getSite(siteName);
        return Response.ok(siteEntity).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getSites(@NotNull @HeaderParam(X_USER_ID) String userId){
        List<CreateSiteRequestDTO> siteEntity = siteService.getSites();
        return Response.ok(siteEntity).build();
    }

    @GET
    @Path("/get-statuses")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getSiteStatuses(@NotNull @HeaderParam(X_USER_ID) String userId){
        return Response.ok(Arrays.asList(SiteStatus.values())).build();
    }

    @PUT
    @Path("/{siteName}/update-site")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response updateSite(@PathParam("siteName") @NotNull String siteName, CreateSiteRequestDTO createSiteRequestDTO, @NotNull @HeaderParam(X_USER_ID) String userId){
        SiteEntity siteEntity = siteService.updateSite(siteName, createSiteRequestDTO);
        return Response.ok(siteEntity).build();
    }
}
