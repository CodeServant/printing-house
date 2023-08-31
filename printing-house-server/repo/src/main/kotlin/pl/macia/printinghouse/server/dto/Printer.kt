package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

const val tablePrinter = "Printer"
const val printerId = "id"
const val printerName = "name"
const val printerDigest = "digest"

@Poko
@Entity
@Table(name = tablePrinter)
class Printer(
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
    constructor(name: String, digest: String) : this(null, name, digest)
}