package org.hkurh.doky.kafka

import com.fasterxml.jackson.annotation.JsonProperty

class SendEmailMessage {
    @JsonProperty("userId")
    var userId: Long? = null

    @JsonProperty("emailType")
    var emailType: EmailType? = null
}

enum class EmailType {
    REGISTRATION,
    RESET_PASSWORD
}
