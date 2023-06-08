package org.bhavani.constructions.dao.api;

import org.bhavani.constructions.dao.entities.UserEntity;

import java.util.Optional;

public interface UserEntityDao {

    void createNewUser(UserEntity userEntity);

    Optional<UserEntity> getUser(String userName);
}
