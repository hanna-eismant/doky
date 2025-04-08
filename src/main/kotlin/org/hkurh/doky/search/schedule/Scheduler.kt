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

package org.hkurh.doky.search.schedule

/**
 * Defines the contract for scheduling operations.
 *
 * The Scheduler interface provides methods for orchestrating and executing
 * predefined tasks on a schedule. It is intended to be implemented by classes
 * which define specific scheduling behavior, particularly for managing
 * background processes or system maintenance tasks.
 */
interface Scheduler {

    /**
     * Performs a complete indexing operation.
     *
     * This method is intended to trigger the full indexing process, which may involve
     * scanning and updating all items or entities managed by the scheduler. It is typically
     * used to ensure that all data is up-to-date and properly synchronized within the system.
     */
    fun fullIndex()
}
