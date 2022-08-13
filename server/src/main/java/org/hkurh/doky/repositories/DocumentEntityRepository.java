package org.hkurh.doky.repositories;

import org.hkurh.doky.entities.DocumentEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface DocumentEntityRepository extends CrudRepository<DocumentEntity, Long>, JpaSpecificationExecutor<DocumentEntity> {
}
