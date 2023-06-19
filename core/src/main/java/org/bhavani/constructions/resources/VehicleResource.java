package org.bhavani.constructions.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.entities.VehicleEntity;
import org.bhavani.constructions.dao.entities.VehicleTaxEntity;
import org.bhavani.constructions.dao.entities.models.VehicleTaxEnum;
import org.bhavani.constructions.dto.CreateVehicleRequestDTO;
import org.bhavani.constructions.dto.UploadVehicleTaxRequestDTO;
import org.bhavani.constructions.services.VehicleService;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.bhavani.constructions.constants.Constants.*;
import static org.bhavani.constructions.utils.VehicleHelper.assignTaxReceiptsForVehicles;

@Path("/vehicles")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
@Api(value = "vehicles")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class VehicleResource {

    private final VehicleService vehicleService;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getVehicles(@NotNull @HeaderParam(X_USER_ID) String userId){
        List<CreateVehicleRequestDTO> vehicles = vehicleService.getVehicles();
        return Response.ok(vehicles).build();
    }

    @GET
    @Path("/get-vehicle-numbers")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getVehicleNumbers(@NotNull @HeaderParam(X_USER_ID) String userId){
        List<CreateVehicleRequestDTO> vehicles = vehicleService.getVehicles();
        return Response.ok(vehicles.stream()
                .map(CreateVehicleRequestDTO::getVehicleNumber)
                .collect(Collectors.toList())).build();
    }

    @GET
    @Path("/tax-types")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getVehicleTaxTypes(){
        EnumSet<VehicleTaxEnum> vehicleTaxes = vehicleService.getVehicleTaxTypes();
        return Response.ok(vehicleTaxes).build();
    }

    @POST
    @Path("/create-new-vehicle")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @UnitOfWork
    public Response createVehicle(@FormDataParam("createVehiclePayload") CreateVehicleRequestDTO createVehicleRequestDTO,
                                  @FormDataParam("vehicleTaxes") List<UploadVehicleTaxRequestDTO> vehicleTaxRequestDTOS,
                                  @FormDataParam("puc") InputStream puc,
                                  @FormDataParam("puc") FormDataContentDisposition pucContent,
                                  @FormDataParam("fitness") InputStream fitness,
                                  @FormDataParam("fitness") FormDataContentDisposition fitnessContent,
                                  @FormDataParam("permit") InputStream permit,
                                  @FormDataParam("permit") FormDataContentDisposition permitContent,
                                  @FormDataParam("insurance") InputStream insurance,
                                  @FormDataParam("insurance") FormDataContentDisposition insuranceContent,
                                  @FormDataParam("tax") InputStream tax,
                                  @FormDataParam("tax") FormDataContentDisposition taxContent,
                                  @FormDataParam("others") InputStream others,
                                  @FormDataParam("others") FormDataContentDisposition othersContent,
                                  @NotNull @HeaderParam(X_USER_ID) String userId){
        assignTaxReceiptsForVehicles(vehicleTaxRequestDTOS, puc, fitness, permit, insurance, tax, others);
        vehicleService.addVehicle(createVehicleRequestDTO, vehicleTaxRequestDTOS);
        return Response.ok(VEHICLE_ADDED_SUCCESSFULLY).build();
    }

    @PUT
    @Path("/update-vehicle")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response updateVehicle(CreateVehicleRequestDTO createVehicleRequestDTO){
        VehicleEntity vehicle = vehicleService.updateVehicle(createVehicleRequestDTO);
        return Response.ok(vehicle).build();
    }

    @GET
    @Path("/{vehicleNumber}/get-vehicle")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getVehicle(@PathParam("vehicleNumber") @NotNull String vehicleNumber){
        VehicleEntity vehicle = vehicleService.getVehicle(vehicleNumber);
        return Response.ok(vehicle).build();
    }

    @DELETE
    @Path("/{vehicleNumber}/delete-vehicle")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response deleteVehicle(@PathParam("vehicleNumber") @NotNull String vehicleNumber){
        vehicleService.delete(vehicleNumber);
        return Response.ok(VEHICLE_DELETED_SUCCESSFULLY).build();
    }

    @POST
    @Path("/{vehicleNumber}/upload-new-vehicle-tax")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @UnitOfWork
    public Response uploadNewTaxDocument(@PathParam("vehicleNumber") @NotNull String vehicleNumber,
                                         @FormDataParam("vehicleTax") UploadVehicleTaxRequestDTO uploadVehicleTaxRequestDTO,
                                         @FormDataParam("taxReceipt") InputStream taxDocument,
                                         @FormDataParam("taxReceipt") FormDataContentDisposition taxDocumentContent){
        uploadVehicleTaxRequestDTO.setTaxReceipt(taxDocument);
        vehicleService.uploadNewTaxDocument(vehicleNumber, uploadVehicleTaxRequestDTO);
        return Response.ok(TAX_DOC_ADDED_SUCCESSFULLY).build();
    }

    @PUT
    @Path("/{vehicleNumber}/tax-type/{taxType}/validity-start-date/{validityStartDate}/update-vehicle-tax")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response updateTaxDocument(@FormDataParam("vehicleTax") UploadVehicleTaxRequestDTO uploadVehicleTaxRequestDTO,
                                      @FormDataParam("taxReceipt") InputStream taxDocument,
                                      @FormDataParam("taxReceipt") FormDataContentDisposition taxDocumentContent,
                                      @PathParam("vehicleNumber") @NotNull String vehicleNumber,
                                      @PathParam("taxType") @NotNull String taxType,
                                      @PathParam("validityStartDate") @NotNull String validityStartDate){
        VehicleTaxEntity vehicleTaxEntity = vehicleService.getVehicleTaxEntry(vehicleNumber, VehicleTaxEnum.valueOf(taxType), LocalDate.parse(validityStartDate));
        uploadVehicleTaxRequestDTO.setTaxReceipt(taxDocument);
        vehicleService.updateVehicleTaxEntry(vehicleTaxEntity, uploadVehicleTaxRequestDTO);
        return Response.ok(vehicleTaxEntity).build();
    }

    @GET
    @Path("/{vehicleNumber}/tax-type/{taxType}/validity-start-date/{validityStartDate}/get-vehicle-tax-details")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getTaxDocument(@PathParam("vehicleNumber") @NotNull String vehicleNumber,
                                   @PathParam("taxType") @NotNull String taxType,
                                   @PathParam("validityStartDate") @NotNull String validityStartDate){
        VehicleTaxEntity vehicleTaxEntity = vehicleService.getVehicleTaxEntry(vehicleNumber, VehicleTaxEnum.valueOf(taxType), LocalDate.parse(validityStartDate));
        return Response.ok(vehicleTaxEntity).build();
    }

    @DELETE
    @Path("/{vehicleNumber}/tax-type/{taxType}/validity-start-date/{validityStartDate}/delete-vehicle-tax")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response deleteTaxDocument(@PathParam("vehicleNumber") @NotNull String vehicleNumber,
                                      @PathParam("taxType") @NotNull String taxType,
                                      @PathParam("validityStartDate") @NotNull String validityStartDate){
        vehicleService.deleteVehicleTaxEntry(vehicleNumber, VehicleTaxEnum.valueOf(taxType), LocalDate.parse(validityStartDate));
        return Response.ok(TAX_DOC_DELETED_SUCCESSFULLY).build();
    }
}
