package org.bhavani.constructions.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.entities.VendorEntity;
import org.bhavani.constructions.dao.entities.models.VendorPurpose;
import org.bhavani.constructions.dto.CreateSupervisorRequestDTO;
import org.bhavani.constructions.dto.CreateVendorRequestDTO;
import org.bhavani.constructions.services.VendorService;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.bhavani.constructions.constants.Constants.COMMODITY_BASE_UNITS;
import static org.bhavani.constructions.constants.Constants.X_USER_ID;

@Path("/vendors")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
@Api(value = "vendors")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class VendorResource {
    private final VendorService vendorService;

    @GET
    @Path("/get-commodity-unit-types")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getCommodityTypes(@NotNull @HeaderParam(X_USER_ID) String userId){
        return Response.ok(COMMODITY_BASE_UNITS).build();
    }

    @GET
    @Path("/get-vendor-purposes")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getVendorPurposes(@NotNull @HeaderParam(X_USER_ID) String userId){
        return Response.ok(Arrays.asList(VendorPurpose.values())).build();
    }

    @POST
    @Path("/create-new-vendor")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response saveVendor(@FormDataParam("createVendorPayLoad") CreateVendorRequestDTO createVendorRequestDTO,
                                   @FormDataParam("contractDocument") InputStream contractDocument,
                                   @FormDataParam("contractDocument") FormDataContentDisposition contractDocumentContent,
                                   @NotNull @HeaderParam(X_USER_ID) String userId){
        VendorEntity vendorEntity = vendorService.createVendor(createVendorRequestDTO, contractDocument, userId);
        return Response.ok(vendorEntity).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getVendors(@NotNull @HeaderParam(X_USER_ID) String userId){
        List<CreateVendorRequestDTO> vendors = vendorService.getVendors();
        return Response.ok(vendors).build();
    }

    @GET
    @Path("/ids")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getVendorIds(@NotNull @HeaderParam(X_USER_ID) String userId){
        List<String> vendorIds = vendorService.getVendorIds();
        return Response.ok(vendorIds).build();
    }
}
