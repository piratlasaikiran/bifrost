package org.bhavani.constructions.serviceImpls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.UserEntityDao;
import org.bhavani.constructions.dao.entities.UserEntity;
import org.bhavani.constructions.services.UserService;

import javax.inject.Inject;

import static org.bhavani.constructions.constants.ErrorConstants.USER_NOT_FOUND;
import static org.bhavani.constructions.utils.EntityBuilder.createUserEntity;
import static org.bhavani.constructions.utils.PasswordHelper.hashPassword;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class DefaultUserService implements UserService {

    private final UserEntityDao userEntityDao;

    @Override
    public String getHashedPassword(String userName) {
        UserEntity userEntity = userEntityDao.getUser(userName).orElseThrow(() -> {
            log.error("No such user found");
            return new RuntimeException(USER_NOT_FOUND);
        });
        return userEntity.getPassword();
    }

    @Override
    public void createNewUser(String userName, String password, String userId) {
        UserEntity userEntity = createUserEntity(userName, hashPassword(password), userId);
        userEntityDao.createNewUser(userEntity);
    }
}
