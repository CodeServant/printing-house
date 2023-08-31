package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*

const val tablePunch = "Punch"
const val punchEnoblingId = "enoblingId"

@Poko
@Entity
@Table(name = tablePunch)
@PrimaryKeyJoinColumn(name = punchEnoblingId)
class Punch(
    name: String,
    descritpion: String?
) : Enobling(name, descritpion)