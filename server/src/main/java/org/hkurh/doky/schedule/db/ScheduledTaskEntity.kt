package org.hkurh.doky.schedule.db

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.lang.NonNull
import java.util.*

@Entity
@Table(name = "scheduled_task")
class ScheduledTaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long = -1

    @Column(name = "name", nullable = false, unique = true)
    var name: String = ""

    @Column(name = "last_run_date")
    var modifiedDate: Date? = null
}
