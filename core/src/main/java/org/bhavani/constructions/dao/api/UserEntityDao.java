package org.bhavani.constructions.dao.api;

import org.bhavani.constructions.dao.entities.UserEntity;

import java.util.Optional;

public interface UserEntityDao  {
    Optional<UserEntity> getUser(String userName);

    void saveUser(UserEntity userEntity);

    void updateUser(UserEntity userEntity);
}
