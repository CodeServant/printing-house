package pl.macia.printinghouse.server.test.dao

/**
 * Generate text value with exact length repeating string of [pad]. Last string can be cropped.
 */
internal fun generateLongName(size: Int, pad: String = "a"): String {
    val times = size / pad.length + 1
    return pad.repeat(times).subSequence(0, size).toString()
}
