package org.hkurh.doky.users;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

public interface UserEntityRepository extends CrudRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    @Query("select u from UserEntity u where u.uid = ?1")
    UserEntity findByUid(@NonNull String uid);
}
