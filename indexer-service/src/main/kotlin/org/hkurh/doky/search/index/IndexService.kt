package org.hkurh.doky.search.index

/**
 * Interface defining the contract for indexing operations.
 *
 * IndexService provides a method for handling full indexing of documents,
 * which may include cleaning existing indexes and re-populating them
 * with data from associated repositories.
 */
interface IndexService {

    /**
     * Executes the process of full indexing for the system.
     *
     * This method is responsible for completely rebuilding the index structure.
     * It may involve cleaning existing indexes and re-populating them with
     * data from their respective data sources or repositories. Typically used
     * in scenarios requiring a fresh or updated index without relying on
     * incremental updates.
     */
    fun fullIndex()
}
