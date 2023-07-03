package org.bhavani.constructions.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.entities.AssetLocationEntity;
import org.bhavani.constructions.dao.entities.AssetOwnershipEntity;
import org.bhavani.constructions.dto.CreateAssetLocationRequestDTO;
import org.bhavani.constructions.dto.CreateAssetOwnershipRequestDTO;
import org.bhavani.constructions.exceptions.OverlappingIntervalsException;
import org.bhavani.constructions.services.AssetManagementService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.List;

import static org.bhavani.constructions.constants.Constants.*;

@Path("/asset-management")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
@Api(value = "asset-management")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class AssetManagementResource {

    private final AssetManagementService assetManagementService;

    @POST
    @Path("/create-asset-location")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response saveAssetLocation(CreateAssetLocationRequestDTO createAssetLocationRequestDTO, @NotNull @HeaderParam(X_USER_ID) String userId)
            throws OverlappingIntervalsException {
        try{
            assetManagementService.saveAssetLocation(createAssetLocationRequestDTO);
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
        AssetLocationEntity assetLocationEntity = assetManagementService.getAssetLocation(assetName, location, LocalDate.parse(startDate));
        return Response.ok(assetLocationEntity).build();
    }

    @GET
    @Path("/asset-locations")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getAssetsLocation(@NotNull @HeaderParam(X_USER_ID) String userId){
        List<CreateAssetLocationRequestDTO> assetLocationRequestDTOs = assetManagementService.getAssetsLocation();
        return Response.ok(assetLocationRequestDTOs).build();
    }

    @GET
    @Path("/asset-ownerships")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getAssetsOwnership(@NotNull @HeaderParam(X_USER_ID) String userId){
        List<CreateAssetOwnershipRequestDTO> assetOwnershipRequestDTOs = assetManagementService.getAssetsOwnership();
        return Response.ok(assetOwnershipRequestDTOs).build();
    }

    @PUT
    @Path("/{assetLocationId}/update-asset-location")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response updateAssetLocation(CreateAssetLocationRequestDTO createAssetLocationRequestDTO,
                               @PathParam("assetLocationId") @NotNull Long assetLocationId,
                               @NotNull @HeaderParam(X_USER_ID) String userId) throws OverlappingIntervalsException {
        AssetLocationEntity assetLocationEntity;
        try{
            assetLocationEntity = assetManagementService.updateAssetLocation(createAssetLocationRequestDTO, assetLocationId);
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
        assetManagementService.deleteAssetLocation(assetName, assetLocation, LocalDate.parse(startDate));
        return Response.ok(ASSET_LOCATION_DELETED_SUCCESSFULLY).build();
    }

    @POST
    @Path("/create-asset-ownership")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response saveAssetOwnership(CreateAssetOwnershipRequestDTO createAssetOwnershipRequestDTO, @NotNull @HeaderParam(X_USER_ID) String userId)
            throws OverlappingIntervalsException {
        try{
            assetManagementService.saveAssetOwnership(createAssetOwnershipRequestDTO, userId);
        }catch (OverlappingIntervalsException exception){
            throw new OverlappingIntervalsException(exception.getMessage());
        }
        return Response.ok(ASSET_LOCATION_ADDED_SUCCESSFULLY).build();
    }

    @PUT
    @Path("/{assetOwnershipId}/update-asset-ownership")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response updateAssetOwnership(CreateAssetOwnershipRequestDTO createAssetOwnershipRequestDTO,
                                        @PathParam("assetOwnershipId") @NotNull Long assetOwnershipId,
                                        @NotNull @HeaderParam(X_USER_ID) String userId) throws OverlappingIntervalsException {
        AssetOwnershipEntity assetOwnershipEntity;
        try{
            assetOwnershipEntity = assetManagementService.updateAssetOwnership(createAssetOwnershipRequestDTO, assetOwnershipId);
        }catch (OverlappingIntervalsException exception){
            throw new OverlappingIntervalsException(exception.getMessage());
        }
        return Response.ok(assetOwnershipEntity).build();
    }
}
