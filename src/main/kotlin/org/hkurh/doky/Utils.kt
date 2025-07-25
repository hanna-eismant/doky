/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2025
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

package org.hkurh.doky

import java.security.SecureRandom

fun String.mask(
    startLength: Int = 3,
    endLength: Int = 3,
    maskChar: Char = '*'
): String {

    if (this.isEmpty() || this.length < startLength + endLength) {
        return maskChar.toString().repeat(this.length)
    }
    val maskLength = this.length - (startLength + endLength)
    return "${this.substring(0, startLength)}${
        maskChar.toString().repeat(maskLength)
    }${this.substring(this.length - endLength)}"
}

fun String?.getExtension(): String {
    return this?.substringAfterLast('.', "") ?: ""

}

fun generateSecureRandomString(length: Int = 10): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    val secureRandom = SecureRandom()
    return (1..length)
        .map { allowedChars[secureRandom.nextInt(allowedChars.size)] }
        .joinToString("")
}
