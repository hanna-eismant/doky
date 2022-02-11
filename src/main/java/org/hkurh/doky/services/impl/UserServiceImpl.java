package org.hkurh.doky.services.impl;

import org.hkurh.doky.entities.UserEntity;
import org.hkurh.doky.exceptions.NotFoundException;
import org.hkurh.doky.repositories.UserEntityRepository;
import org.hkurh.doky.services.UserService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    private UserEntityRepository userEntityRepository;

    @Override
    public UserEntity findUserByUid(@NonNull final String userUid) {
        final UserEntity userEntity = getUserEntityRepository().findByUid(userUid);
        if (userEntity == null) {
            throw new NotFoundException("User doesn't exist");
        }
        return userEntity;
    }

    @Override
    public boolean checkUserExistence(@NonNull final String userUid) {
        final UserEntity userEntity = getUserEntityRepository().findByUid(userUid);
        return userEntity != null;
    }

    @Override
    public UserEntity create(@NonNull final String userUid, @NonNull final String encodedPassword) {
        final UserEntity userEntity = new UserEntity();
        userEntity.setUid(userUid);
        userEntity.setPassword(encodedPassword);
        return getUserEntityRepository().save(userEntity);
    }

    private UserEntityRepository getUserEntityRepository() {
        return userEntityRepository;
    }

    @Resource
    public void setUserEntityRepository(final UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }
}
