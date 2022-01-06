package org.hkurh.doky.services;

import javax.annotation.Resource;

import org.hkurh.doky.entities.UserEntity;
import org.hkurh.doky.exceptions.NotFoundException;
import org.hkurh.doky.repositories.UserEntityRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserEntityRepository userEntityRepository;

    @Override
    public UserEntity findUserByUid(@NonNull final String userUid) {
        UserEntity userEntity = getUserEntityRepository().findByUid(userUid);
        if (userEntity == null) {
            throw new NotFoundException("User doesn't exist");
        }
        return userEntity;
    }

    @Override
    public boolean checkUser(@NonNull final String userUid, @NonNull final String password) {
        UserEntity userEntity = getUserEntityRepository().findByUid(userUid);
        if (userEntity == null) {
            return false;
        }
        return password.equals(userEntity.getPassword());
    }

    public UserEntityRepository getUserEntityRepository() {
        return userEntityRepository;
    }

    @Resource
    public void setUserEntityRepository(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }
}
