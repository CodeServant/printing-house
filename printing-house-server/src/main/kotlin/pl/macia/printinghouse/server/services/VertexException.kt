package pl.macia.printinghouse.server.services

/**
 * This is thrown when vertex can't be created.
 */
class VertexException(msg: String?) : Exception(msg) {
    constructor() : this(null)
}