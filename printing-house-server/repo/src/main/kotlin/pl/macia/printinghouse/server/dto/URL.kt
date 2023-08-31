package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import org.hibernate.validator.constraints.URL as AURL

@Poko
@Entity
@Table(name = URL.TABLE_NAME)
internal class URL(
    @Id
    @Column(name = ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    @Column(name = URL)
    @field:AURL
    var url: String
) {
    constructor(url: String) : this(null, url)

    companion object {
        const val TABLE_NAME = "URL"
        const val ID = "id"
        const val URL = "url"
    }
}