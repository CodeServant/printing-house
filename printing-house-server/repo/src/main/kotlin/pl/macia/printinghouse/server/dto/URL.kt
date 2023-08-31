package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import org.hibernate.validator.constraints.URL as AURL

@Poko
@Entity
@Table(name = URL.tableURL)
internal class URL(
    @Id
    @Column(name = urlId)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    @Column(name = urlUrl)
    @field:AURL
    var url: String
) {
    constructor(url: String) : this(null, url)

    companion object {
        const val tableURL = "URL"
        const val urlId = "id"
        const val urlUrl = "url"
    }
}