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
