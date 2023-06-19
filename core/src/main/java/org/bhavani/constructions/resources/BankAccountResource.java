package org.bhavani.constructions.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.entities.BankAccountEntity;
import org.bhavani.constructions.dto.CreateBankAccountRequestDTO;
import org.bhavani.constructions.services.BankAccountService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.bhavani.constructions.constants.Constants.X_USER_ID;

@Path("/bank-accounts")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
@Api(value = "bank-accounts")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class BankAccountResource {

    private final BankAccountService bankAccountService;

    @POST
    @Path("/create-new-account")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response saveBankAccount(CreateBankAccountRequestDTO createBankAccountRequestDTO, @NotNull @HeaderParam(X_USER_ID) String userId){
        BankAccountEntity bankAccount = bankAccountService.createBankAccount(createBankAccountRequestDTO, userId);
        return Response.ok(bankAccount).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getSites(@NotNull @HeaderParam(X_USER_ID) String userId){
        List<CreateBankAccountRequestDTO> bankAccountRequestDTOS = bankAccountService.getBankAccounts();
        return Response.ok(bankAccountRequestDTOS).build();
    }

    @GET
    @Path("/atm-cards")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getATMCards(@NotNull @HeaderParam(X_USER_ID) String userId){
        List<Long> atmCards= bankAccountService.getATMCards();
        return Response.ok(atmCards).build();
    }
}
