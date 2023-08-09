package org.hkurh.doky.documents;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface DocumentEntityRepository extends CrudRepository<DocumentEntity, Long>, JpaSpecificationExecutor<DocumentEntity> {
    @Query("select d from DocumentEntity d where d.creator.id = ?1")
    List<DocumentEntity> findByCreatorId(@NonNull Long documentId);

    @Query("select d from DocumentEntity d where d.id = ?1 and d.creator.id = ?2")
    Optional<DocumentEntity> findByIdAndCreatorId(@NonNull Long documentId, @NonNull Long creatorId);
}
