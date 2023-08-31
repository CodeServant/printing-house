package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*

@Poko
@Entity
@Table(name = Punch.tablePunch)
@PrimaryKeyJoinColumn(name = Punch.punchEnoblingId)
class Punch(
    name: String,
    descritpion: String?
) : Enobling(name, descritpion) {
    companion object {
        const val tablePunch = "Punch"
        const val punchEnoblingId = "enoblingId"
    }
}