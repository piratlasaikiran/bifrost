package org.bhavani.constructions.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.entities.TransactionEntity;
import org.bhavani.constructions.dao.entities.models.TransactionMode;
import org.bhavani.constructions.dto.CreateTransactionRequestDTO;
import org.bhavani.constructions.services.TransactionService;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

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
    public Response saveBankAccount(@FormDataParam("createTransactionPayload") CreateTransactionRequestDTO createTransactionRequestDTO,
                                    @FormDataParam("receipt") InputStream receipt,
                                    @FormDataParam("receipt") FormDataContentDisposition receiptContent,
                                    @NotNull @HeaderParam(X_USER_ID) String userId){
        TransactionEntity transaction = transactionService.createTransaction(createTransactionRequestDTO, receipt, userId);
        return Response.ok(transaction).build();
    }
}
