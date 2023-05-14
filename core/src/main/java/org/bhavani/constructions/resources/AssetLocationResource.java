package org.bhavani.constructions.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.entities.AssetLocationEntity;
import org.bhavani.constructions.dao.entities.SiteEntity;
import org.bhavani.constructions.dto.CreateAssetLocationRequestDTO;
import org.bhavani.constructions.dto.CreateSiteRequestDTO;
import org.bhavani.constructions.services.AssetLocationService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.bhavani.constructions.constants.Constants.*;

@Path("/asset-location")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
@Api(value = "asset-location")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class AssetLocationResource {

    private final AssetLocationService assetLocationService;

    @POST
    @Path("/create-asset-location")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response saveAssetLocation(CreateAssetLocationRequestDTO createAssetLocationRequestDTO, @NotNull @HeaderParam(X_USER_ID) String userId){
        assetLocationService.saveAssetLocation(createAssetLocationRequestDTO);
        return Response.ok(ASSET_LOCATION_ADDED_SUCCESSFULLY).build();
    }

    @GET
    @Path("/{assetName}/get-asset-location")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getAssetLocation(@PathParam("assetName") @NotNull String assetName){
        AssetLocationEntity assetLocationEntity = assetLocationService.getAssetLocation(assetName);
        return Response.ok(assetLocationEntity).build();
    }

    @PUT
    @Path("/{assetName}/update-asset")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response updateAssetLocation(CreateAssetLocationRequestDTO createAssetLocationRequestDTO,
                               @PathParam("assetName") @NotNull String assetName,
                               @NotNull @HeaderParam(X_USER_ID) String userId){
        AssetLocationEntity assetLocationEntity = assetLocationService.updateAssetLocation(createAssetLocationRequestDTO, assetName);
        return Response.ok(assetLocationEntity).build();
    }

    @DELETE
    @Path("/{assetName}/asset-location/{assetLocation}/start-date/{startDate}/delete-asset")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response deleteAssetLocation(@PathParam("assetName") @NotNull String assetName,
                                        @PathParam("assetLocation") @NotNull String assetLocation,
                                        @PathParam("startDate") @NotNull String startDate,
                                        @NotNull @HeaderParam(X_USER_ID) String userId){
        assetLocationService.deleteAssetLocation(assetName, assetLocation, startDate);
        return Response.ok(ASSET_LOCATION_DELETED_SUCCESSFULLY).build();
    }


}
