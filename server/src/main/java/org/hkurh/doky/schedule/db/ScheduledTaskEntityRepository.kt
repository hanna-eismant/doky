package org.hkurh.doky.schedule.db;

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface ScheduledTaskEntityRepository : JpaRepository<ScheduledTaskEntity, Long>,
    JpaSpecificationExecutor<ScheduledTaskEntity> {


    fun findByName(name: String): ScheduledTaskEntity

}
