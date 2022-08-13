package org.hkurh.doky.services.impl;

import org.hkurh.doky.entities.UserEntity;
import org.hkurh.doky.exceptions.NotFoundException;
import org.hkurh.doky.repositories.UserEntityRepository;
import org.hkurh.doky.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserEntityRepository userEntityRepository;

    @Override
    public UserEntity findUserByUid(@NonNull String userUid) {
        var userEntity = getUserEntityRepository().findByUid(userUid);
        if (userEntity == null) {
            throw new NotFoundException("User doesn't exist");
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
        return getUserEntityRepository().save(userEntity);
    }

    @Override
    public UserEntity getCurrentUser() {
        var name = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserEntityRepository().findByUid(name);
    }

    private UserEntityRepository getUserEntityRepository() {
        return userEntityRepository;
    }

    @Autowired
    public void setUserEntityRepository(final UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }
}
