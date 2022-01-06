package org.hkurh.doky.repositories;

import org.hkurh.doky.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserEntityRepository extends CrudRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    @Query("select u from UserEntity u where u.uid = ?1")
    UserEntity findByUid(String uid);
}
