package org.hkurh.doky.users.db

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hkurh.doky.security.UserAuthority

@Entity
@Table(
    name = "authorities",
    uniqueConstraints = [UniqueConstraint(name = "uc_authorities_authority", columnNames = ["authority"])]
)
class AuthorityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long = -1

    @Enumerated(EnumType.STRING)
    @Column(name = "authority", nullable = false, unique = true)
    var authority: UserAuthority = UserAuthority.ROLE_USER
}
