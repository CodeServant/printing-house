package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

@Poko
@Entity
@Table(name = Printer.TABLE_NAME)
internal class Printer(
    @Id
    @Column(name = ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,
    @field:NotEmpty
    @field:Size(max = 100)
    @Column(name = NAME)
    var name: String,
    @field:NotEmpty
    @field:Size(max = 4)
    @Column(name = DIGEST)
    var digest: String
) {
    companion object {
        const val TABLE_NAME = "Printer"
        const val ID = "id"
        const val NAME = "name"
        const val DIGEST = "digest"
    }

    constructor(name: String, digest: String) : this(null, name, digest)
}