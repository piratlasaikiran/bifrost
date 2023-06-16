package org.bhavani.constructions.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.entities.AssetLocationEntity;
import org.bhavani.constructions.dao.entities.SiteEntity;
import org.bhavani.constructions.dto.CreateAssetLocationRequestDTO;
import org.bhavani.constructions.dto.CreateSiteRequestDTO;
import org.bhavani.constructions.exceptions.OverlappingIntervalsException;
import org.bhavani.constructions.services.AssetLocationService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.List;

import static org.bhavani.constructions.constants.Constants.*;

@Path("/asset-locations")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
@Api(value = "asset-locations")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class AssetLocationResource {

    private final AssetLocationService assetLocationService;

    @POST
    @Path("/create-asset-location")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response saveAssetLocation(CreateAssetLocationRequestDTO createAssetLocationRequestDTO, @NotNull @HeaderParam(X_USER_ID) String userId)
            throws OverlappingIntervalsException {
        try{
            assetLocationService.saveAssetLocation(createAssetLocationRequestDTO);
        }catch (OverlappingIntervalsException exception){
            throw new OverlappingIntervalsException(exception.getMessage());
        }
        return Response.ok(ASSET_LOCATION_ADDED_SUCCESSFULLY).build();
    }

    @GET
    @Path("/{assetName}/get-asset-location")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getAssetLocation(@NotNull @PathParam("assetName") String assetName,
                                     @NotNull @QueryParam("location") String location,
                                     @NotNull @QueryParam("startDate") String startDate){
        AssetLocationEntity assetLocationEntity = assetLocationService.getAssetLocation(assetName, location, LocalDate.parse(startDate));
        return Response.ok(assetLocationEntity).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getAssetsLocation(@NotNull @HeaderParam(X_USER_ID) String userId){
        List<CreateAssetLocationRequestDTO> assetLocationRequestDTOs = assetLocationService.getAssetsLocation();
        return Response.ok(assetLocationRequestDTOs).build();
    }

    @PUT
    @Path("/{assetName}/update-asset")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response updateAssetLocation(CreateAssetLocationRequestDTO createAssetLocationRequestDTO,
                               @PathParam("assetName") @NotNull String assetName,
                               @NotNull @HeaderParam(X_USER_ID) String userId) throws OverlappingIntervalsException {
        AssetLocationEntity assetLocationEntity;
        try{
            assetLocationEntity = assetLocationService.updateAssetLocation(createAssetLocationRequestDTO, assetName);
        }catch (OverlappingIntervalsException exception){
            throw new OverlappingIntervalsException(exception.getMessage());
        }
        return Response.ok(assetLocationEntity).build();
    }

    @DELETE
    @Path("/{assetName}/delete-asset")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response deleteAssetLocation(@PathParam("assetName") @NotNull String assetName,
                                        @QueryParam("assetLocation") @NotNull String assetLocation,
                                        @QueryParam("startDate") @NotNull String startDate,
                                        @NotNull @HeaderParam(X_USER_ID) String userId){
        assetLocationService.deleteAssetLocation(assetName, assetLocation, LocalDate.parse(startDate));
        return Response.ok(ASSET_LOCATION_DELETED_SUCCESSFULLY).build();
    }


}
