package org.bhavani.constructions.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.entities.UserEntity;
import org.bhavani.constructions.services.UserService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Singleton
@Api(value = "users")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class UserResource {
    private final UserService userService;

    @GET
    @Path("/users/{userName}/get-user")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response getUser(@Nonnull String userName){
        try{
            UserEntity userEntity = userService.getUser(userName);
            return Response.ok(userEntity).build();
        }catch (Exception e){
            log.error("Error while fetching user: {}", userName);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

}
