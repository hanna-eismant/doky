package org.hkurh.doky.users;

import org.hkurh.doky.errorhandling.DokyNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserEntityRepository userEntityRepository;

    @Override
    public UserEntity findUserByUid(@NonNull String userUid) {
        var userEntity = getUserEntityRepository().findByUid(userUid);
        if (userEntity == null) {
            throw new DokyNotFoundException("User doesn't exist");
        }
        return userEntity;
    }

    @Override
    public boolean checkUserExistence(@NonNull String userUid) {
        var userEntity = getUserEntityRepository().findByUid(userUid);
        return userEntity != null;
    }

    @Override
    public UserEntity create(@NonNull String userUid, @NonNull String encodedPassword) {
        var userEntity = new UserEntity();
        userEntity.setUid(userUid);
        userEntity.setPassword(encodedPassword);
        var createdUser = getUserEntityRepository().save(userEntity);
        LOG.debug(format("Created new user [%s]", createdUser.getId()));
        return createdUser;
    }

    @Override
    public UserEntity getCurrentUser() {
        var name = SecurityContextHolder.getContext().getAuthentication().getName();
        var userEntity = getUserEntityRepository().findByUid(name);
        LOG.debug(format("Get current user [%s]", userEntity.getId()));
        return userEntity;
    }

    private UserEntityRepository getUserEntityRepository() {
        return userEntityRepository;
    }

    @Autowired
    public void setUserEntityRepository(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }
}
