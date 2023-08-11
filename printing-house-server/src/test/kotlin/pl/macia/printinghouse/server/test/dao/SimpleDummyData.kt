package pl.macia.printinghouse.server.test.dao

/**
 * Generate text value with exact length repeating string of [padd]. Last string can be cropped.
 */
fun generateLongName(size: Int, padd: String = "a"):String {
    val times = size/padd.length+1
    return padd.repeat(times).subSequence(0, size).toString()
}