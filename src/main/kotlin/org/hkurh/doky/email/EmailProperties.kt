/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2005
 *  - Hanna Kurhuzenkava (hanna.kuehuzenkava@outlook.com)
 *  - Anton Kurhuzenkau (kurguzenkov@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

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
