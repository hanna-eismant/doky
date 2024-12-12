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
    name = "reset_password_tokens",
    indexes = [Index(name = "idx_reset_password_tokens_token", columnList = "token")],
    uniqueConstraints = [
        UniqueConstraint(name = "uc_reset_password_tokens_token", columnNames = ["token"]),
        UniqueConstraint(name = "uc_reset_password_tokens_app_user", columnNames = ["app_user"])]
)
class ResetPasswordTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long = -1

    @OneToOne
    @JoinColumn(name = "app_user", nullable = false, unique = true)
    lateinit var user: UserEntity

    @Column(name = "token", nullable = false, unique = true)
    lateinit var token: String

    @Column(name = "expiration_date", nullable = false)
    lateinit var expirationDate: Date
}
