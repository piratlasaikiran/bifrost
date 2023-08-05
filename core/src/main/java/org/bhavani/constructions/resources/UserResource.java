package org.bhavani.constructions.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dto.LoginRequestDTO;
import org.bhavani.constructions.services.UserService;
import org.bhavani.constructions.utils.PasswordHelper;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.bhavani.constructions.constants.Constants.X_USER_ID;

@Path("/users")
@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "users")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class UserResource {

    private final UserService userService;

    @POST
    @Path("/login")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @UnitOfWork
    public Response login(@FormDataParam("userLoginPayload") LoginRequestDTO loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        String storedHashedPassword = getUserHashedPassword(username);

        if (storedHashedPassword != null && PasswordHelper.verifyPassword(password, storedHashedPassword)) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    @Path("/create-new-user")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @UnitOfWork
    public Response createNewUser(@FormDataParam("userLoginPayload") LoginRequestDTO request,
                                  @NotNull @HeaderParam(X_USER_ID) String userId) {
        String username = request.getUsername();
        String password = request.getPassword();
        userService.createNewUser(username, password, userId);
        return Response.ok(Response.Status.CREATED).build();
    }

    private String getUserHashedPassword(String username) {
        return userService.getHashedPassword(username);
    }
}

