package org.hkurh.doky.users.db

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "users",
    indexes = [Index(name = "idx_users_uid", columnList = "uid")],
    uniqueConstraints = [UniqueConstraint(name = "uc_users_uid", columnNames = ["uid"])]
)
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_authorities",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "authority_id", referencedColumnName = "id")]
    )
    var authorities: MutableSet<AuthorityEntity> = HashSet()
}
