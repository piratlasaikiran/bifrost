package org.bhavani.constructions.helpers;

import org.bhavani.constructions.dao.entities.UserEntity;

public class EntityBuilder {
    public static UserEntity createUserEntity(String userName, Long mobileNumber, Boolean isAdmin){
        return UserEntity.builder()
                .name(userName)
                .mobileNumber(mobileNumber)
                .admin(isAdmin)
                .build();
    }
}
