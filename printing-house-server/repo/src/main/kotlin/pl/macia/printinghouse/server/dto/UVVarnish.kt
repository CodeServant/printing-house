package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.Entity
import jakarta.persistence.PrimaryKeyJoinColumn
import jakarta.persistence.Table

@Poko
@Entity
@Table(name = UVVarnish.tableUVVarnish)
@PrimaryKeyJoinColumn(name = UVVarnish.uvVarnishEnoblingId)
class UVVarnish(
    name: String,
    descritpion: String?
) : Enobling(name, descritpion) {
    companion object {
        const val tableUVVarnish = "UVVarnish"
        const val uvVarnishEnoblingId = "enoblingId"
    }
}