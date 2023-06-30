package org.bhavani.constructions.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.entities.TransactionEntity;
import org.bhavani.constructions.dao.entities.models.TransactionMode;
import org.bhavani.constructions.dao.entities.models.TransactionPurpose;
import org.bhavani.constructions.dao.entities.models.TransactionStatus;
import org.bhavani.constructions.dto.CreateTransactionRequestDTO;
import org.bhavani.constructions.dto.PassBookResponseDTO;
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
import java.util.List;

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
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getAllTransactions(@NotNull @HeaderParam(X_USER_ID) String userId){
        List<CreateTransactionRequestDTO> transactions = transactionService.getAllTransactions();
        return Response.ok(transactions).build();
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

    @GET
    @Path("/transaction-statuses")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getTransactionStatuses(@NotNull @HeaderParam(X_USER_ID) String userId){
        EnumSet<TransactionStatus> transactionStatuses = transactionService.getTransactionStatuses();
        return Response.ok(transactionStatuses).build();
    }

    @GET
    @Path("/passbook-main-pages")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getAllPassBookMainPages(@NotNull @HeaderParam(X_USER_ID) String userId){
        List<PassBookResponseDTO> passBooks = transactionService.getAllPassBookMainPages();
        return Response.ok(passBooks).build();
    }

    @GET
    @Path("/passbooks/{accountName}")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getAllPassBookMainPages(@NotNull @PathParam("accountName") String accountName,
                                            @NotNull @HeaderParam(X_USER_ID) String userId){
        List<PassBookResponseDTO> passBooks = transactionService.getAccountPassBook(accountName);
        return Response.ok(passBooks).build();
    }
}
