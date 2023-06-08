package org.bhavani.constructions.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.services.UserService;
import org.bhavani.constructions.utils.PasswordHelper;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    public Response login(@FormDataParam("userLoginPayload") PasswordHelper.LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

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
    public Response createNewUser(@FormDataParam("userLoginPayload") PasswordHelper.LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        userService.createNewUser(username, password);
        return Response.ok(Response.Status.CREATED).build();
    }

    private String getUserHashedPassword(String username) {
        return userService.getHashedPassword(username);
    }
}

