package org.hkurh.doky.users

import jakarta.persistence.*

@Entity
@Table(name = "user")
class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "uid", nullable = false, unique = true)
    var uid: String? = null

    @Column(name = "name")
    var name: String? = null

    @Column(name = "password", nullable = false)
    var password: String? = null
}
