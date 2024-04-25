package org.hkurh.doky.password.db

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hkurh.doky.users.db.UserEntity
import java.util.*

@Entity
@Table(
    name = "reset_password_token",
    indexes = [Index(name = "idx_reset_password_token_token", columnList = "token")],
    uniqueConstraints = [UniqueConstraint(name = "uq_reset_password_token_token", columnNames = ["token"])]
)
class ResetPasswordTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long = -1

    @OneToOne
    @JoinColumn(name = "user")
    var user: UserEntity? = null

    @Column(name = "token")
    var token: String? = null

    @Column(name = "expiration_date")
    var expirationDate: Date? = null
}
