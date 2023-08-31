package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Poko
@Entity
@Table(name = Printer.tablePrinter)
internal class Printer(
    @Id
    @Column(name = printerId)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,
    @field:NotBlank
    @field:Size(max = 100)
    @Column(name = printerName)
    var name: String,
    @field:NotBlank
    @field:Size(max = 4)
    @Column(name = printerDigest)
    var digest: String
) {
    companion object {
        const val tablePrinter = "Printer"
        const val printerId = "id"
        const val printerName = "name"
        const val printerDigest = "digest"
    }

    constructor(name: String, digest: String) : this(null, name, digest)
}