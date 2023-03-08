package org.bhavani.constructions.services;

import org.bhavani.constructions.dao.entities.UserEntity;

import java.util.Optional;

public interface UserService {
    UserEntity getUser(String userName);

    void createNewUser(String userName, Long mobileNumber, Boolean isAdmin);
}
