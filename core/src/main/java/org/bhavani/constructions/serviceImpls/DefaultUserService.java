package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.UserEntityDao;
import org.bhavani.constructions.dao.entities.UserEntity;
import org.bhavani.constructions.services.UserService;

import javax.inject.Inject;
import java.util.Optional;

import static org.bhavani.constructions.constants.Constants.MOBILE_NUMBER_LENGTH;
import static org.bhavani.constructions.constants.ErrorConstants.INVALID_MOBILE_NUMBER;
import static org.bhavani.constructions.constants.ErrorConstants.USER_NOT_FOUND;
import static org.bhavani.constructions.helpers.EntityBuilder.createUserEntity;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class DefaultUserService implements UserService {

    private final UserEntityDao userEntityDao;

    @Override
    public UserEntity getUser(String userName) {
        Optional<UserEntity> userEntity = userEntityDao.getUser(userName);
        if(!userEntity.isPresent()){
            log.info("No user with name: {}", userName);
            throw new IllegalArgumentException(USER_NOT_FOUND);
        }
        return userEntity.get();
    }

    @Override
    public void createNewUser(String userName, String mobileNumber, Boolean isAdmin) {
        if(String.valueOf(mobileNumber).length() != MOBILE_NUMBER_LENGTH)
            throw new IllegalArgumentException(INVALID_MOBILE_NUMBER);
        UserEntity newUser = createUserEntity(userName, mobileNumber, isAdmin);
        userEntityDao.saveUser(newUser);
    }
}
