package org.hkurh.doky.users

import jakarta.persistence.*

@Entity
@Table(name = "user",
        indexes = [Index(name = "idx_userentity_uid", columnList = "uid")],
        uniqueConstraints = [UniqueConstraint(name = "uc_userentity_uid", columnNames = ["uid"])])
class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long = -1

    @Column(name = "uid", nullable = false, unique = true)
    var uid: String = ""

    @Column(name = "name")
    var name: String? = null

    @Column(name = "password", nullable = false)
    var password: String = ""
}
