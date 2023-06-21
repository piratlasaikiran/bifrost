package org.bhavani.constructions.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.entities.TransactionEntity;
import org.bhavani.constructions.dao.entities.models.TransactionMode;
import org.bhavani.constructions.dao.entities.models.TransactionPurpose;
import org.bhavani.constructions.dao.entities.models.VehicleTaxEnum;
import org.bhavani.constructions.dto.CreateTransactionRequestDTO;
import org.bhavani.constructions.services.TransactionService;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.EnumSet;

import static org.bhavani.constructions.constants.Constants.X_USER_ID;

@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
@Api(value = "transactions")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class TransactionResource {

    private final TransactionService transactionService;

    @POST
    @Path("/create-new-transaction")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response saveTransaction(@FormDataParam("createTransactionPayload") CreateTransactionRequestDTO createTransactionRequestDTO,
                                    @FormDataParam("bill") InputStream bill,
                                    @FormDataParam("bill") FormDataContentDisposition billContent,
                                    @NotNull @HeaderParam(X_USER_ID) String userId){
        TransactionEntity transaction = transactionService.createTransaction(createTransactionRequestDTO, bill, userId);
        return Response.ok(transaction).build();
    }

    @GET
    @Path("/transaction-modes")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getTransactionModes(@NotNull @HeaderParam(X_USER_ID) String userId){
        EnumSet<TransactionMode> transactionModes = transactionService.getTransactionModes();
        return Response.ok(transactionModes).build();
    }

    @GET
    @Path("/transaction-purposes")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getTransactionPurposes(@NotNull @HeaderParam(X_USER_ID) String userId){
        EnumSet<TransactionPurpose> transactionPurposes = transactionService.getTransactionPurposes();
        return Response.ok(transactionPurposes).build();
    }
}
