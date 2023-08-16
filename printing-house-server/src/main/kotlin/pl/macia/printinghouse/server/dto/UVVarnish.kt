package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.Entity
import jakarta.persistence.PrimaryKeyJoinColumn
import jakarta.persistence.Table

const val tableUVVarnish = "UVVarnish"
const val uvVarnishEnoblingId = "enoblingId"

@Poko
@Entity
@Table(name = tableUVVarnish)
@PrimaryKeyJoinColumn(name = uvVarnishEnoblingId)
class UVVarnish(
    name: String,
    descritpion: String?
) : Enobling(name, descritpion)