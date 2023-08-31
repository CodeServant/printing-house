package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.Entity
import jakarta.persistence.PrimaryKeyJoinColumn
import jakarta.persistence.Table

@Poko
@Entity
@Table(name = UVVarnish.TABLE_NAME)
@PrimaryKeyJoinColumn(name = UVVarnish.ENOBLING_ID)
internal class UVVarnish(
    name: String,
    descritpion: String?
) : Enobling(name, descritpion) {
    companion object {
        const val TABLE_NAME = "UVVarnish"
        const val ENOBLING_ID = "enoblingId"
    }
}