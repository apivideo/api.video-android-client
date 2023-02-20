package video.api.client.api.work

import androidx.work.Operation
import androidx.work.WorkRequest

/**
 * A class that holds an [Operation] and a [WorkRequest] to be able to manipulate the [WorkRequest].
 *
 * @param operation The [Operation] to manipulate the [WorkRequest].
 * @param request The [WorkRequest] to manipulate.
 */
data class OperationWithRequest(
    val operation: Operation,
    val request: WorkRequest
)