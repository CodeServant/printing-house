package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*

@Poko
@Entity
@Table(name = Punch.TABLE_NAME)
@PrimaryKeyJoinColumn(name = Punch.ENOBLING_ID)
internal class Punch(
    name: String,
    descritpion: String?
) : Enobling(name, descritpion) {
    companion object {
        const val TABLE_NAME = "Punch"
        const val ENOBLING_ID = "enoblingId"
    }
}