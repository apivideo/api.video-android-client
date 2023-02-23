package video.api.client.api.work.stores

import video.api.client.api.upload.IProgressiveUploadSession

/**
 * A store for [IProgressiveUploadSession]s.
 */
internal object ProgressiveUploadSessionStore {
    private val sessions = mutableSetOf<IProgressiveUploadSession>()

    /**
     * Adds a session to the store and return its index.
     *
     * @param session The session to add.
     * @return The index of the session in the store.s
     */
    fun add(session: IProgressiveUploadSession): Int {
        sessions.add(session)
        return sessions.indexOf(session)
    }

    /**
     * Gets a session from the store.
     *
     * @param index The index of the session in the store.
     * @return The session at the given index.
     */
    fun get(index: Int): IProgressiveUploadSession? {
        return sessions.elementAtOrNull(index)
    }

    /**
     * Gets the index of a session in the store.
     *
     * @param session The session to get the index of.
     * @return The index of the session in the store.
     */
    fun indexOf(session: IProgressiveUploadSession): Int {
        return sessions.indexOf(session)
    }
}