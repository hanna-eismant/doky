package org.hkurh.doky.kafka

import com.fasterxml.jackson.annotation.JsonProperty

class SendEmailMessage {
    @JsonProperty("userId")
    var userId: Long? = null

    @JsonProperty("emailType")
    var emailType: EmailType? = null
    override fun toString(): String {
        return "SendEmailMessage(userId=$userId, emailType=$emailType)"
    }
}

enum class EmailType {
    REGISTRATION,
    RESET_PASSWORD
}
