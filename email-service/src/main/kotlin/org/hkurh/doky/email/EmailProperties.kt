package org.hkurh.doky.email

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "doky.email")
class EmailProperties {
    lateinit var sender: Sender
    lateinit var registration: Registration
    lateinit var resetPassword: ResetPassword
    lateinit var sendgrid: Sendgrid

    class Sender {
        lateinit var email: String
        lateinit var name: String
    }

    class Registration {
        lateinit var subject: String
        lateinit var sendgrid: Sendgrid

        class Sendgrid {
            lateinit var templateId: String
        }
    }

    class ResetPassword {
        lateinit var subject: String
        lateinit var sendgrid: Sendgrid

        class Sendgrid {
            lateinit var templateId: String
        }
    }

    class Sendgrid {
        lateinit var apiKey: String
    }
}
